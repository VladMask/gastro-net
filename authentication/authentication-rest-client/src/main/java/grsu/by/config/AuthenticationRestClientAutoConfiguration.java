package grsu.by.config;

import grsu.by.AuthenticationRestClient;
import grsu.by.config.properties.AuthenticationRestClientProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
@ConditionalOnClass(AuthenticationRestClient.class)
@EnableConfigurationProperties(AuthenticationRestClientProperties.class)
public class AuthenticationRestClientAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public AuthenticationRestClient authenticationRestClient(AuthenticationRestClientProperties properties, RestClient.Builder restClientBuilder) {
        return new AuthenticationRestClient(properties, restClientBuilder);
    }
}
