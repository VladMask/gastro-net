package grsu.by.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "stripe")
public class StripeProperties {
    private String secretKey;
    private String webhookSecret;
    private String publishableKey;
    private String currency;
}