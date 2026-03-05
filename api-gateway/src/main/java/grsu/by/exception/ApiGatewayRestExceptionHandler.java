package grsu.by.exception;

import grsu.by.ExceptionDtoFactory;
import grsu.by.dto.ExceptionMessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

@RestControllerAdvice
@RequiredArgsConstructor
public class ApiGatewayRestExceptionHandler {
    private final ExceptionDtoFactory exceptionDtoFactory;

    @ExceptionHandler({HttpClientErrorException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionMessageDto handleHttpClientErrorException(HttpClientErrorException exception) {
        return exception.getResponseBodyAs(ExceptionMessageDto.class);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public ExceptionMessageDto handleIllegalStateException(MethodArgumentNotValidException exception) {
        return exceptionDtoFactory.build(HttpStatus.NOT_ACCEPTABLE, exception);
    }
}
