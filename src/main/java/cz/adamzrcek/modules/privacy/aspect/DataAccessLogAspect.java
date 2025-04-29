package cz.adamzrcek.modules.privacy.aspect;

import cz.adamzrcek.modules.auth.security.model.CustomUserPrincipal;
import cz.adamzrcek.modules.privacy.annotation.LogRepository;
import cz.adamzrcek.modules.privacy.entity.DataAccessLogs;
import cz.adamzrcek.modules.privacy.repository.DataAccessLogsRepository;
import cz.adamzrcek.modules.user.entity.User;
import cz.adamzrcek.modules.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.*;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class DataAccessLogAspect {

    private final DataAccessLogsRepository dataAccessLogsRepository;
    private final UserRepository userRepository;

    private static final ThreadLocal<Boolean> currentlyLogging = ThreadLocal.withInitial(() -> false);

    @AfterReturning(pointcut = "execution(* cz.adamzrcek.modules..repository.*Repository+.*(..))", returning = "result")
    public void logRepositoryAccess(JoinPoint joinPoint, Object result) {
        // Prevent recursion
        if (currentlyLogging.get()) return;

        try {
            currentlyLogging.set(true);

            if (shouldSkipRepository(joinPoint)) return;

            Class<?> repositoryInterface = getRepositoryInterface(joinPoint.getTarget().getClass());
            if (repositoryInterface == null) return;

            LogRepository logRepository = repositoryInterface.getAnnotation(LogRepository.class);
            if (logRepository == null) return;

            String methodName = joinPoint.getSignature().getName();
            if (isMethodExcluded(methodName, logRepository.excludedMethods())) return;

            String action = determineActionFromMethodName(methodName);
            logDataAccess(joinPoint, logRepository.entityType(), action, result);

        } catch (Exception e) {
            log.error("Error during repository access logging", e);
        } finally {
            currentlyLogging.set(false);
        }
    }

    private void logDataAccess(JoinPoint joinPoint, String entityType, String action, Object result) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Long userId = extractUserIdFromAuthentication(authentication);
            Long entityId = extractEntityId(joinPoint.getArgs(), result);
            String ipAddress = extractClientIpAddress();

            DataAccessLogs logEntry = DataAccessLogs.builder()
                    .action(action)
                    .entityType(entityType)
                    .entityId(entityId != null ? entityId : 0L)
                    .userId(userId)
                    .ipAddress(ipAddress)
                    .timestamp(LocalDateTime.now())
                    .details(createDetailMessage(joinPoint))
                    .build();

            dataAccessLogsRepository.save(logEntry);

            log.debug("Repository access logged: userId={}, action={}, entityType={}, entityId={}",
                    userId, action, entityType, entityId);

        } catch (Exception e) {
            log.error("Failed to log repository access", e);
        }
    }

    private boolean shouldSkipRepository(JoinPoint joinPoint) {
        String targetClassName = joinPoint.getTarget().getClass().getSimpleName();
        return targetClassName.contains("DataAccessLogsRepository") || targetClassName.contains("UserRepository");
    }

    private String createDetailMessage(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        return String.format("Repository operation: %s.%s", signature.getDeclaringType().getSimpleName(), signature.getName());
    }

    private Long extractEntityId(Object[] args, Object result) {
        if (result == null) return tryExtractFromArgs(args);

        if (result instanceof Optional<?> optionalResult && optionalResult.isPresent()) {
            return tryExtractId(optionalResult.get());
        }

        if (result instanceof Collection<?> collection && !collection.isEmpty()) {
            return tryExtractId(collection.iterator().next());
        }

        return tryExtractId(result);
    }

    private Long tryExtractFromArgs(Object[] args) {
        if (args == null) return null;
        for (Object arg : args) {
            Long id = tryExtractId(arg);
            if (id != null) return id;
        }
        return null;
    }

    private Long tryExtractId(Object obj) {
        if (obj == null) return null;
        try {
            Method getIdMethod = obj.getClass().getMethod("getId");
            Object id = getIdMethod.invoke(obj);
            return id instanceof Long ? (Long) id : null;
        } catch (Exception ignored) {
            return null;
        }
    }

    private Long extractUserIdFromAuthentication(Authentication authentication) {
        if (authentication != null
                && authentication.isAuthenticated()
                && authentication.getPrincipal() instanceof CustomUserPrincipal principal) {
            return principal.id();
        }
        return null;
    }

    private String extractClientIpAddress() {
        try {
            RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
            if (requestAttributes instanceof ServletRequestAttributes servletRequestAttributes) {
                HttpServletRequest request = servletRequestAttributes.getRequest();
                return request.getRemoteAddr();
            }
        } catch (Exception e) {
            log.warn("Failed to extract client IP address", e);
        }
        return null;
    }

    private boolean isMethodExcluded(String methodName, String[] excludedMethods) {
        return Arrays.asList(excludedMethods).contains(methodName);
    }

    private String determineActionFromMethodName(String methodName) {
        if (methodName.startsWith("find") || methodName.startsWith("get") || methodName.startsWith("count") || methodName.startsWith("exists")) {
            return "READ";
        } else if (methodName.startsWith("save") || methodName.startsWith("create")) {
            return "CREATE/UPDATE";
        } else if (methodName.startsWith("update") || methodName.startsWith("modify")) {
            return "UPDATE";
        } else if (methodName.startsWith("delete") || methodName.startsWith("remove")) {
            return "DELETE";
        }
        return "UNKNOWN";
    }

    private Class<?> getRepositoryInterface(Class<?> aClass) {
        if (aClass == null) return null;
        for (Class<?> iface : aClass.getInterfaces()) {
            if (iface.isAnnotationPresent(LogRepository.class)) return iface;
            Class<?> recursive = getRepositoryInterface(iface);
            if (recursive != null) return recursive;
        }
        return null;
    }
}
