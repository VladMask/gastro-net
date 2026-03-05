package grsu.by.config;

import grsu.by.PaymentRestClient;
import grsu.by.config.properties.PaymentRestClientProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
@ConditionalOnClass(PaymentRestClient.class)
@EnableConfigurationProperties(PaymentRestClientProperties.class)
public class PaymentRestClientAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public PaymentRestClient paymentRestClient(PaymentRestClientProperties properties, RestClient.Builder restClientBuilder) {
        return new PaymentRestClient(properties, restClientBuilder);
    }
}
