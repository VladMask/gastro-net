package grsu.by.config;

import grsu.by.NotificationRestClient;
import grsu.by.config.properties.NotificationRestClientProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
@ConditionalOnClass(NotificationRestClient.class)
@EnableConfigurationProperties(NotificationRestClientProperties.class)
public class NotificationRestClientAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public NotificationRestClient notificationRestClient(NotificationRestClientProperties properties, RestClient.Builder restClientBuilder) {
        return new NotificationRestClient(properties, restClientBuilder);
    }
}
