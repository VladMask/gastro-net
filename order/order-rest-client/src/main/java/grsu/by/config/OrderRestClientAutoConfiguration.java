package grsu.by.config;

import grsu.by.OrderRestClient;
import grsu.by.config.properties.OrderRestClientProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
@ConditionalOnClass(OrderRestClient.class)
@EnableConfigurationProperties(OrderRestClientProperties.class)
public class OrderRestClientAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public OrderRestClient orderRestClient(OrderRestClientProperties properties, RestClient.Builder restClientBuilder) {
        return new OrderRestClient(properties, restClientBuilder);
    }
}
