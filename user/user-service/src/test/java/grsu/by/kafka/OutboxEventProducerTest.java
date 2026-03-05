package grsu.by.kafka;

import grsu.by.config.properties.OutboxProperties;
import grsu.by.entity.OutboxEvent;
import grsu.by.enums.EventStatus;
import grsu.by.service.OutboxEventService;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.TopicPartition;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Limit;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@ExtendWith(MockitoExtension.class)
class OutboxEventProducerTest {

    @Mock
    private OutboxProperties.Producer outboxProperties;
    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;
    @Mock
    private OutboxEventService outboxEventService;
    @InjectMocks
    private OutboxEventProducer outboxEventProducer;

    private List<OutboxEvent> getOutboxEvents() {
        return List.of(
                        new OutboxEvent("Header_0", "Payload_0"),
                        new OutboxEvent("Header_1", "Payload_1")
        );
    }

    @Test
    void sendUserCreatedEvents() {
        List<OutboxEvent> events = getOutboxEvents();

        Mockito.when(outboxProperties.getKafkaTopic()).thenReturn("topic");
        Mockito.when(outboxEventService.findByStatusOrderByCreatedAt(EventStatus.NEW, Limit.of(10))).thenReturn(events);
        Mockito.when(kafkaTemplate.send(Mockito.anyString(), Mockito.eq("Header_0"), Mockito.anyString()))
                        .thenReturn(CompletableFuture.completedFuture(
                                new SendResult<>(
                                        new ProducerRecord<>(
                                                "user_event",
                                                "record"
                                        ),
                                        new RecordMetadata(
                                                new TopicPartition("user_event", 0),
                                                0,
                                                0,
                                                0,
                                                0,
                                                0
                                        )
                                )
                        ));
        Mockito.when(kafkaTemplate.send(Mockito.anyString(), Mockito.eq("Header_1"), Mockito.anyString()))
                .thenReturn(CompletableFuture.failedFuture(new RuntimeException()));

        outboxEventProducer.sendUserCreatedEvents();

        Mockito.verify(outboxEventService, Mockito.times(1))
                .findByStatusOrderByCreatedAt(EventStatus.NEW, Limit.of(10));
        Mockito.verify(outboxEventService, Mockito.times(1))
                .saveAll(events);
        Mockito.verify(kafkaTemplate, Mockito.times(2))
                .send(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());

        OutboxEvent first = events.get(0);
        OutboxEvent second = events.get(1);

        Assertions.assertEquals(EventStatus.SUCCESS, first.getStatus());
        Assertions.assertEquals(EventStatus.NEW, second.getStatus());
        Assertions.assertEquals(1, second.getRetry());

    }

}