package grsu.by.exception;

import grsu.by.ExceptionDtoFactory;
import grsu.by.dto.ExceptionMessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
public class InvoiceServiceRestExceptionHandler {

    private final ExceptionDtoFactory exceptionDtoFactory;

    @ExceptionHandler({InvoiceGenerationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionMessageDto handleEntityNotFoundException(InvoiceGenerationException exception) {
        return exceptionDtoFactory.build(HttpStatus.BAD_REQUEST, exception);
    }

}
