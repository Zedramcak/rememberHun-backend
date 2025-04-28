package cz.adamzrcek.modules.privacy.aspect;

import cz.adamzrcek.modules.privacy.annotation.LogDataAccess;
import cz.adamzrcek.modules.privacy.entity.DataAccessLogs;
import cz.adamzrcek.modules.privacy.repository.DataAccessLogsRepository;
import cz.adamzrcek.modules.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class DataAccessLogAspect {

    private final DataAccessLogsRepository dataAccessLogsRepository;
    private final UserRepository userRepository;

    @AfterReturning("@annotation(logDataAccess)")
    public void logAccess(JoinPoint joinPoint, LogDataAccess logDataAccess) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()&& !"anonymousUser".equals(authentication.getPrincipal())
        ){
            String email = authentication.getName();

            userRepository.findByEmail(email)
                    .ifPresent(user -> {
                        DataAccessLogs logEntry = createLogEntry(joinPoint, logDataAccess, user.getId());
                        dataAccessLogsRepository.save(logEntry);
                        log.info("Data access logged: userId={}, action={}, entityType={}",
                                logEntry.getUserId(), logEntry.getAction(), logEntry.getEntityType());

                    });
            return;
        }

        DataAccessLogs logEntry = createLogEntry(joinPoint, logDataAccess, null);
        dataAccessLogsRepository.save(logEntry);
        log.info("Data access logged: userId={}, action={}, entityType={}", logEntry.getUserId(), logEntry.getAction(), logEntry.getEntityType());
    }

    private DataAccessLogs createLogEntry(JoinPoint joinPoint, LogDataAccess logDataAccess, Long userId) {
        String methodName = joinPoint.getSignature().getName();

        return DataAccessLogs.builder()
                .action(logDataAccess.action())
                .entityType(logDataAccess.entity())
                .entityId(extractEntityId(joinPoint.getArgs()))
                .userId(userId)
                .timestamp(LocalDateTime.now())
                .details("Accessed method: " + methodName + " with parameters: " + Arrays.toString(joinPoint.getArgs()))
                .build();
    }


    private Long extractEntityId(Object[] args) {
        if (args != null && args.length > 0 && args[0] instanceof Long) {
            return (Long) args[0];
        }
        return null;
    }
}
