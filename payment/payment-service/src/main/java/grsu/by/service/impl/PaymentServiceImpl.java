package grsu.by.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;
import com.stripe.param.PaymentIntentCreateParams;
import grsu.by.OrderRestClient;
import grsu.by.config.properties.StripeProperties;
import grsu.by.dto.orderDto.OrderShortDto;
import grsu.by.dto.paymentDto.PaymentShortDto;
import grsu.by.dto.paymentDto.StripeSessionDto;
import grsu.by.entity.OutboxEvent;
import grsu.by.entity.Payment;
import grsu.by.enums.PaymentMethod;
import grsu.by.enums.PaymentStatus;
import grsu.by.repository.PaymentRepository;
import grsu.by.service.OutboxEventService;
import grsu.by.service.PaymentService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRestClient orderRestClient;
    private final ModelMapper mapper;
    private final StripeProperties stripeProperties;
    private final OutboxEventService outboxEventService;
    private final ObjectMapper objectMapper;

    @Transactional
    @Override
    public PaymentShortDto createPayment(Long orderId) {
        if (paymentRepository.existsByOrderId(orderId)) {
            throw new IllegalStateException("Payment for this order already exists");
        }
        OrderShortDto order;
        try {
            order = orderRestClient.findOrderById(orderId);
        } catch (HttpClientErrorException e) {
            throw new EntityNotFoundException("Order was not found");
        }
        Payment payment = Payment.builder()
                .orderId(orderId)
                .restaurantId(order.getRestaurantId())
                .userId(order.getUserId())
                .paymentMethod(PaymentMethod.CARD)
                .status(PaymentStatus.PENDING_CONFIRMATION)
                .amount(order.getTotalPrice())
                .build();
        return mapper.map(paymentRepository.save(payment), PaymentShortDto.class);
    }

    @Transactional
    @Override
    public StripeSessionDto createStripeSession(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new EntityNotFoundException("Payment not found"));
        if (payment.getStripePaymentIntentId() != null) {
            try {
                PaymentIntent existing = PaymentIntent.retrieve(payment.getStripePaymentIntentId());
                return StripeSessionDto.builder()
                        .clientSecret(existing.getClientSecret())
                        .stripePaymentIntentId(existing.getId())
                        .paymentId(paymentId)
                        .build();
            } catch (StripeException e) {
                log.error("Failed to retrieve Stripe PaymentIntent", e);
                throw new IllegalStateException("Stripe error: " + e.getMessage());
            }
        }
        long amountInCents = payment.getAmount()
                .multiply(java.math.BigDecimal.valueOf(100))
                .longValue();
        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(amountInCents)
                .setCurrency(stripeProperties.getCurrency())
                .putMetadata("paymentId", String.valueOf(paymentId))
                .putMetadata("orderId", String.valueOf(payment.getOrderId()))
                .setAutomaticPaymentMethods(
                        PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                                .setEnabled(true)
                                .build())
                .build();
        try {
            PaymentIntent intent = PaymentIntent.create(params);
            payment.setStripePaymentIntentId(intent.getId());
            paymentRepository.save(payment);
            return StripeSessionDto.builder()
                    .clientSecret(intent.getClientSecret())
                    .stripePaymentIntentId(intent.getId())
                    .paymentId(paymentId)
                    .build();
        } catch (StripeException e) {
            log.error("Failed to create Stripe PaymentIntent", e);
            payment.setStatus(PaymentStatus.ERROR);
            paymentRepository.save(payment);
            throw new IllegalStateException("Stripe error: " + e.getMessage());
        }
    }

    @Transactional
    @Override
    public boolean confirmPayment(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Payment not found"));
        if (payment.getStatus() == PaymentStatus.CONFIRMED) return true;
        if (payment.getStatus() != PaymentStatus.PENDING_CONFIRMATION) {
            throw new IllegalStateException("Cannot confirm payment in status: " + payment.getStatus());
        }
        payment.setStatus(PaymentStatus.CONFIRMED);
        paymentRepository.save(payment);
        return true;
    }

    @Transactional
    @Override
    public boolean cancelPayment(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Payment not found"));
        if (payment.getStatus() == PaymentStatus.CANCELLED) return true;
        if (payment.getStatus() == PaymentStatus.CONFIRMED) {
            throw new IllegalStateException("Cannot cancel already confirmed payment");
        }
        if (payment.getStripePaymentIntentId() != null) {
            try {
                PaymentIntent intent = PaymentIntent.retrieve(payment.getStripePaymentIntentId());
                if (!"succeeded".equals(intent.getStatus()) && !"canceled".equals(intent.getStatus())) {
                    intent.cancel();
                }
            } catch (StripeException e) {
                log.warn("Failed to cancel Stripe PaymentIntent: {}", e.getMessage());
            }
        }
        payment.setStatus(PaymentStatus.CANCELLED);
        paymentRepository.save(payment);
        return true;
    }

    @Transactional
    @Override
    public boolean handleStripeWebhook(String payload, String sigHeader) {
        Event event;
        try {
            event = Webhook.constructEvent(payload, sigHeader, stripeProperties.getWebhookSecret());
        } catch (SignatureVerificationException e) {
            log.warn("Invalid Stripe webhook signature: {}", e.getMessage());
            return false;
        }
        log.info("Stripe event: {}", event.getType());
        switch (event.getType()) {
            case "payment_intent.succeeded" -> handleWebhookIntent(event, PaymentStatus.CONFIRMED);
            case "payment_intent.payment_failed" -> handleWebhookIntent(event, PaymentStatus.ERROR);
            default -> log.debug("Unhandled Stripe event: {}", event.getType());
        }
        return true;
    }

    private void handleWebhookIntent(Event event, PaymentStatus newStatus) {
        PaymentIntent intent = (PaymentIntent) event.getDataObjectDeserializer()
                .getObject().orElseThrow();
        String paymentIdStr = intent.getMetadata().get("paymentId");
        if (paymentIdStr == null) return;
        paymentRepository.findById(Long.parseLong(paymentIdStr)).ifPresent(p -> {
            p.setStatus(newStatus);
            Payment saved = paymentRepository.save(p);
            log.info("Payment {} -> {} via webhook", p.getId(), newStatus);
            if (newStatus == PaymentStatus.CONFIRMED) {
                produceEvent(saved, "payment.succeeded");
            } else if (newStatus == PaymentStatus.ERROR) {
                produceEvent(saved, "payment.failed");
            }
        });
    }

    private void produceEvent(Payment payment, String header) {
        try {
            outboxEventService.create(new OutboxEvent(
                    header,
                    objectMapper.writeValueAsString(payment))
            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}