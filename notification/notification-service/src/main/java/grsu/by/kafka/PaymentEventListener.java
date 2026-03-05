package grsu.by.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import grsu.by.dto.PaymentEventDto;
import grsu.by.service.ConfirmationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class PaymentEventListener {
    private final ObjectMapper objectMapper;
    private final ConfirmationService confirmationService;
    private static final String TARGET_MESSAGE_KEY = "Payment created";

    @KafkaListener(topics = "${notification-service.consumer.kafka-topic}",
            groupId = "${spring.kafka.consumer.group-id}")
    public void listen(ConsumerRecord<String, String> record) throws JsonProcessingException {
        log.info("Received record: \nkey: {} \nvalue: {}",  record.key(), record.value());
        if(TARGET_MESSAGE_KEY.equals(record.key())) {
            confirmationService.sendVerificationCode(
                    objectMapper.readValue(record.value(), PaymentEventDto.class)
            );
        }
    }
}
