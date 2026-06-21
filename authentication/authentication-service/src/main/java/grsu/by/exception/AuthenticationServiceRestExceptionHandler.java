package grsu.by.exception;

import grsu.by.ExceptionDtoFactory;
import grsu.by.dto.ExceptionMessageDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.naming.AuthenticationException;

@RestControllerAdvice
@RequiredArgsConstructor
public class AuthenticationServiceRestExceptionHandler {
    private final ExceptionDtoFactory exceptionDtoFactory;

    @ExceptionHandler({EntityNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionMessageDto handleEntityNotFoundException(EntityNotFoundException exception) {
        return exceptionDtoFactory.build(HttpStatus.NOT_FOUND, exception);
    }

    @ExceptionHandler({AuthenticationException.class})
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public ExceptionMessageDto handleAuthenticationException(AuthenticationException exception) {
        return exceptionDtoFactory.build(HttpStatus.NOT_ACCEPTABLE, exception);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public ExceptionMessageDto handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        return exceptionDtoFactory.build(HttpStatus.NOT_ACCEPTABLE, exception);
    }

    @ExceptionHandler({IllegalStateException.class})
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public ExceptionMessageDto handleIllegalStateException(IllegalStateException exception) {
        return exceptionDtoFactory.build(HttpStatus.NOT_ACCEPTABLE, exception);
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ExceptionMessageDto handleAccessDeniedException(AccessDeniedException exception) {
        return exceptionDtoFactory.build(HttpStatus.FORBIDDEN, exception);
    }
}
