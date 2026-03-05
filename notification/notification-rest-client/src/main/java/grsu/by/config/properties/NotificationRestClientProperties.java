package grsu.by.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "rest.client.notification-service")
public class NotificationRestClientProperties {
    private String baseUrl;
}
