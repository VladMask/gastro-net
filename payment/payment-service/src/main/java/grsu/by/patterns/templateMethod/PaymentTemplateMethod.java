package grsu.by.patterns.templateMethod;

import com.fasterxml.jackson.databind.ObjectMapper;
import grsu.by.dto.paymentDto.PaymentShortDto;
import grsu.by.entity.OutboxEvent;
import grsu.by.entity.Payment;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@RequiredArgsConstructor
public abstract class PaymentTemplateMethod {
    protected final ObjectMapper objectMapper;

    @SneakyThrows
    public PaymentShortDto makePayment(Long orderId) {
        Payment payment = constructPayment(orderId);
        PaymentShortDto saved = savePayment(payment);
        createOutboxEvent(new OutboxEvent(
                "Payment created",
                objectMapper.writeValueAsString(payment)
        ));
        return saved;
    }

    protected abstract Payment constructPayment(Long orderId);
    protected abstract PaymentShortDto savePayment(Payment payment);
    protected abstract void createOutboxEvent(OutboxEvent outboxEvent);
}
