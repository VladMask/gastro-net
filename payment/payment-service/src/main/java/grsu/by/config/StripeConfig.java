package grsu.by.config;

import com.stripe.Stripe;
import grsu.by.config.properties.StripeProperties;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(StripeProperties.class)
@RequiredArgsConstructor
public class StripeConfig {

    private final StripeProperties stripeProperties;

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeProperties.getSecretKey();
    }
}