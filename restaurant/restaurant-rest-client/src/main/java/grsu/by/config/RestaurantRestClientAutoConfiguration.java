package grsu.by.config;

import grsu.by.RestaurantRestClient;
import grsu.by.config.properties.RestaurantRestClientProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
@ConditionalOnClass(RestaurantRestClient.class)
@EnableConfigurationProperties(RestaurantRestClientProperties.class)
public class RestaurantRestClientAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public RestaurantRestClient restaurantRestClient(RestaurantRestClientProperties properties, RestClient.Builder restClientBuilder) {
        return new RestaurantRestClient(properties, restClientBuilder);
    }
}
