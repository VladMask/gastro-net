package grsu.by.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "exception.dto-factory")
public class ExceptionDtoFactoryProperties {
    private String serviceId;
}
