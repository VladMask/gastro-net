package grsu.by.config;

import grsu.by.ReservationRestClient;
import grsu.by.config.properties.ReservationRestClientProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
@ConditionalOnClass(ReservationRestClient.class)
@EnableConfigurationProperties(ReservationRestClientProperties.class)
public class ReservationRestClientAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public ReservationRestClient reservationRestClient(ReservationRestClientProperties properties, RestClient.Builder restClientBuilder) {
        return new ReservationRestClient(properties, restClientBuilder);
    }
}
