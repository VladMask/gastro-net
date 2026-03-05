package grsu.by.kafka;

import grsu.by.config.properties.OutboxProperties;
import grsu.by.entity.OutboxEvent;
import grsu.by.enums.EventStatus;
import grsu.by.service.OutboxEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Limit;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;


@RequiredArgsConstructor
@Slf4j
public class OutboxEventProducer {
    private final OutboxProperties.Producer properties;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final OutboxEventService outboxEventService;

    @Transactional
    public void sendUserCreatedEvents() {
        List<OutboxEvent> events = outboxEventService.findByStatusOrderByCreatedAt(EventStatus.NEW, Limit.of(10));
        List<CompletableFuture<SendResult<String, String>>> futures  = new ArrayList<>();
        events.forEach(event -> futures.add(sendUserCreatedEvent(event)));
        try {
            futures.forEach(CompletableFuture::join);
        } catch (CompletionException | CancellationException e) {
            log.error("{} during Kafka sends in {}", e.getClass().getName(), this.getClass().getName(), e);
        }
        outboxEventService.saveAll(events);
    }

    private CompletableFuture<SendResult<String, String>> sendUserCreatedEvent(OutboxEvent event) {
        return kafkaTemplate.send(properties.getKafkaTopic(), event.getHeader(), event.getPayload())
                .whenComplete((r, e) -> {
                        if (e == null) {
                            log.info("Sent event to kafka topic: {}", properties.getKafkaTopic());
                            event.setStatus(EventStatus.SUCCESS);
                        }
                        else  {
                            log.error("Failed to send event to Kafka", e);
                            event.setRetry((short) (event.getRetry() + 1));
                        }
                    }
                );
    }
}
