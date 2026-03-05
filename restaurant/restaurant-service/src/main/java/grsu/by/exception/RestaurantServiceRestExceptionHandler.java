package grsu.by.exception;

import grsu.by.ExceptionDtoFactory;
import grsu.by.dto.ExceptionMessageDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
public class RestaurantServiceRestExceptionHandler {

    private final ExceptionDtoFactory exceptionDtoFactory;

    @ExceptionHandler({EntityNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionMessageDto handleEntityNotFoundException(EntityNotFoundException exception) {
        return exceptionDtoFactory.build(HttpStatus.NOT_FOUND, exception);
    }

}
