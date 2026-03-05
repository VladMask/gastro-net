package grsu.by.config;

import grsu.by.UserRestClient;
import grsu.by.config.properties.UserRestClientProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
@ConditionalOnClass(UserRestClient.class)
@EnableConfigurationProperties(UserRestClientProperties.class)
public class UserRestClientAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public UserRestClient userRestClient(UserRestClientProperties properties, RestClient.Builder restClientBuilder) {
        return new UserRestClient(properties, restClientBuilder);
    }
}
