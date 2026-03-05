package grsu.by;

import grsu.by.config.properties.ExceptionDtoFactoryProperties;
import grsu.by.dto.ExceptionMessageDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.Instant;

@Getter
@Setter
public class ExceptionDtoFactory {
    private final ExceptionDtoFactoryProperties properties;

    public ExceptionDtoFactory(ExceptionDtoFactoryProperties properties) {
        this.properties = properties;
    }

    public ExceptionMessageDto build(HttpStatus status, Exception exception) {
        return ExceptionMessageDto.builder()
                .status(status)
                .code(status.value())
                .serviceId(properties.getServiceId())
                .message(exception.getMessage())
                .date(Instant.now().toString())
                .build();
    }
}
