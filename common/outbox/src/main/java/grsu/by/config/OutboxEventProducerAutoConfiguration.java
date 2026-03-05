package grsu.by.config;

import grsu.by.config.properties.OutboxProperties;
import grsu.by.kafka.OutboxEventProducer;
import grsu.by.service.OutboxEventService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;

@Configuration
@ConditionalOnClass(OutboxEventProducer.class)
@EnableConfigurationProperties(OutboxProperties.class)
public class OutboxEventProducerAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public OutboxEventProducer outboxEventProducer(
            OutboxProperties properties,
            KafkaTemplate<String, String> kafkaTemplate,
            OutboxEventService outboxEventService
    ) {
        return new OutboxEventProducer(properties.getProducer(), kafkaTemplate, outboxEventService);
    }
}
