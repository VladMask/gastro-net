package grsu.by.patterns.templateMethod.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import grsu.by.dto.orderDto.OrderShortDto;
import grsu.by.dto.paymentDto.PaymentInvoiceDto;
import grsu.by.dto.paymentDto.PaymentShortDto;
import grsu.by.dto.reservationDto.ReservationFullDto;
import grsu.by.entity.OutboxEvent;
import grsu.by.entity.Payment;
import grsu.by.enums.PaymentMethod;
import grsu.by.enums.PaymentStatus;
import grsu.by.patterns.facade.RestClientFacade;
import grsu.by.patterns.templateMethod.PaymentTemplateMethod;
import grsu.by.service.OutboxEventService;
import grsu.by.service.PaymentService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

@Component
public class PaymentProcessor extends PaymentTemplateMethod {
    private final RestClientFacade restClientFacade;
    private final OutboxEventService outboxEventService;
    private final PaymentService paymentService;

    public PaymentProcessor(
            RestClientFacade restClientFacade,
            OutboxEventService outboxEventService,
            PaymentService paymentService,
            ObjectMapper objectMapper) {
        super(objectMapper);
        this.restClientFacade = restClientFacade;
        this.outboxEventService = outboxEventService;
        this.paymentService = paymentService;
    }

    @Override
    protected Payment constructPayment(Long orderId) {
        OrderShortDto order;
        try {
            order = restClientFacade.findOrderById(orderId);
        } catch (HttpClientErrorException e) {
            throw new EntityNotFoundException("Order was not found");
        }

        ReservationFullDto reservation;
        reservation = restClientFacade.findReservationById(order.getReservationId());

        return Payment.builder()
                .orderId(orderId)
                .restaurantId(order.getRestaurantId())
                .userId(reservation.getUserId())
                .paymentMethod(PaymentMethod.CARD)
                .status(PaymentStatus.PENDING_CONFIRMATION)
                .amount(order.getTotalPrice())
                .build();
    }

    @Override
    protected PaymentShortDto savePayment(Payment payment) {
        return paymentService.savePayment(payment);
    }

    @Override
    protected void createOutboxEvent(OutboxEvent outboxEvent) {
        outboxEventService.create(outboxEvent);
    }

    public boolean confirmPayment(Long id) {
        return paymentService.confirmPayment(id);
    }

    public PaymentInvoiceDto getInvoiceDataById(Long id) {
        return paymentService.getInvoiceDataById(id);
    }
}
