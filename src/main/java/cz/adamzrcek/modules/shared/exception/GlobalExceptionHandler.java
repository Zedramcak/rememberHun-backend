package cz.adamzrcek.modules.shared.exception;

import cz.adamzrcek.modules.shared.dtos.ErrorResponse;
import cz.adamzrcek.modules.auth.exception.EmailAlreadyExistsException;
import cz.adamzrcek.modules.auth.exception.ExpiredTokenException;
import cz.adamzrcek.modules.auth.exception.InvalidPasswordException;
import cz.adamzrcek.modules.auth.exception.NotAllowedException;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleEmailAlreadyExistsException(EmailAlreadyExistsException ex){
        log.error("⚠️ Registration failed: {}", ex.getMessage());

        ErrorResponse response = ErrorResponse
                .builder()
                .code("email-already-exists")
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<ErrorResponse> handleInvalidPasswordException(InvalidPasswordException ex){
        log.error("⚠️ Login failed: {}", ex.getMessage());
        ErrorResponse response = ErrorResponse
                .builder()
                .code("invalid-password")
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex){
        log.error("⚠️ Resource not found: {}", ex.getMessage());
        ErrorResponse response = ErrorResponse
                .builder()
                .code("resource-not-found")
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NotAllowedException.class)
    public ResponseEntity<ErrorResponse> handleNotAllowedException(NotAllowedException ex){
        log.error("⚠️ User is not allowed to do this: {}", ex.getMessage());
        ErrorResponse response = ErrorResponse
                .builder()
                .code("not-allowed")
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException ex){
        log.error("⚠️ User did something bad: {}", ex.getMessage());
        ErrorResponse response = ErrorResponse
                .builder()
                .code("bad-request")
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAuthorizationDeniedException(AuthorizationDeniedException ex){
        log.error("🧱 Access denied: {}", ex.getMessage());
        ErrorResponse response = ErrorResponse
                .builder()
                .code("forbidden")
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ExpiredTokenException.class)
    public ResponseEntity<ErrorResponse> handleExpiredTokenException(ExpiredTokenException ex){
        log.error("🔒 Token expired: {}", ex.getMessage());
        ErrorResponse response = ErrorResponse
                .builder()
                .code("token-expired")
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex){
        log.error("😱 Internal server error: {}", ex.getMessage());
        ErrorResponse response = ErrorResponse
                .builder()
                .code("internal-server-error")
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
