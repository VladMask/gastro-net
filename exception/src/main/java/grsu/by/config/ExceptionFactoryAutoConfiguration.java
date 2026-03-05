package grsu.by.config;

import grsu.by.ExceptionDtoFactory;
import grsu.by.config.properties.ExceptionDtoFactoryProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(ExceptionDtoFactory.class)
@EnableConfigurationProperties(ExceptionDtoFactoryProperties.class)
public class ExceptionFactoryAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ExceptionDtoFactory exceptionFactory(ExceptionDtoFactoryProperties properties) {
        return new ExceptionDtoFactory(properties);
    }
}
