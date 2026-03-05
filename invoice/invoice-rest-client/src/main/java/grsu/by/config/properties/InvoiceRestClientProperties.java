package grsu.by.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "rest.client.invoice-service")
public class InvoiceRestClientProperties {
    private String baseUrl;
}
