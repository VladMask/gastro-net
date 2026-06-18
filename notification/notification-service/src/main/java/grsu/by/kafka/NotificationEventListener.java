package grsu.by.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import grsu.by.dto.ReservationEventDto;
import grsu.by.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Component
@Slf4j
@RequiredArgsConstructor
public class NotificationEventListener {

    private final ObjectMapper objectMapper;
    private final NotificationService notificationService;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter
            .ofPattern("dd.MM.yyyy HH:mm")
            .withZone(ZoneId.of("Europe/Moscow"));

    @KafkaListener(
            topics = "#{'${notification-service.consumer.kafka-topic}'.split(',')}"
    )
    public void listen(ConsumerRecord<String, String> record) {
        log.info("Received event key={}, value={}", record.key(), record.value());
        try {
            switch (record.key()) {
                case "reservation.created" -> {
                    ReservationEventDto dto = objectMapper.readValue(record.value(), ReservationEventDto.class);
                    String date = FORMATTER.format(Instant.parse(dto.getReservedAt()));
                    notificationService.create(
                            dto.getUserId(),
                            "Бронирование создано",
                            "Ваше бронирование #" + dto.getId() + " на " + date +
                                    " (" + dto.getGuestsCount() + " гостей) ожидает подтверждения.",
                            "RESERVATION_CREATED");
                }
                case "reservation.confirmed" -> {
                    ReservationEventDto dto = objectMapper.readValue(record.value(), ReservationEventDto.class);
                    String date = FORMATTER.format(Instant.parse(dto.getReservedAt()));
                    notificationService.create(
                            dto.getUserId(),
                            "Бронирование подтверждено",
                            "Ваше бронирование #" + dto.getId() + " на " + date + " подтверждено рестораном.",
                            "RESERVATION_CONFIRMED");
                }
                case "reservation.cancelled" -> {
                    ReservationEventDto dto = objectMapper.readValue(record.value(), ReservationEventDto.class);
                    String date = FORMATTER.format(Instant.parse(dto.getReservedAt()));
                    notificationService.create(
                            dto.getUserId(),
                            "Бронирование отменено",
                            "Ваше бронирование #" + dto.getId() + " на " + date + " было отменено.",
                            "RESERVATION_CANCELLED");
                }
                default -> log.debug("Unhandled event key: {}", record.key());
            }
        } catch (JsonProcessingException e) {
            log.error("Failed to parse event payload for key={}", record.key(), e);
        }
    }
}