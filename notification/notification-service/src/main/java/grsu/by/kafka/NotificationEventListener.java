package grsu.by.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import grsu.by.dto.NotificationEventDto;
import grsu.by.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class NotificationEventListener {

    private final ObjectMapper objectMapper;
    private final NotificationService notificationService;

    @KafkaListener(
            topics = "#{'${notification-service.consumer.kafka-topic}'.split(',')}"
    )
    public void listen(ConsumerRecord<String, String> record) {
        log.info("Received event key={}", record.key());
        try {
            NotificationEventDto dto = objectMapper.readValue(
                    record.value(), NotificationEventDto.class);
            switch (record.key()) {
                case "payment.succeeded" -> notificationService.create(
                        dto.getUserId(),
                        "Оплата прошла успешно",
                        "Ваш заказ #" + dto.getOrderId() + " успешно оплачен.",
                        "PAYMENT_SUCCESS");

                case "payment.failed" -> notificationService.create(
                        dto.getUserId(),
                        "Ошибка оплаты",
                        "Оплата заказа #" + dto.getOrderId() + " не удалась. Попробуйте снова.",
                        "PAYMENT_FAILED");

                case "reservation.created" -> notificationService.create(
                        dto.getUserId(),
                        "Бронирование создано",
                        "Ваше бронирование #" + dto.getReservationId() + " ожидает подтверждения.",
                        "RESERVATION_CREATED");

                case "reservation.confirmed" -> notificationService.create(
                        dto.getUserId(),
                        "Бронирование подтверждено",
                        "Ваше бронирование #" + dto.getReservationId() + " подтверждено рестораном.",
                        "RESERVATION_CONFIRMED");

                case "reservation.cancelled" -> notificationService.create(
                        dto.getUserId(),
                        "Бронирование отменено",
                        "Ваше бронирование #" + dto.getReservationId() + " было отменено.",
                        "RESERVATION_CANCELLED");

                default -> log.debug("Unhandled event key: {}", record.key());
            }
        } catch (JsonProcessingException e) {
            log.error("Failed to parse event payload for key={}", record.key(), e);
        }
    }
}