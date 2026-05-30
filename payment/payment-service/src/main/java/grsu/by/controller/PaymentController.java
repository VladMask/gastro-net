package grsu.by.controller;

import grsu.by.dto.paymentDto.PaymentShortDto;
import grsu.by.dto.paymentDto.StripeSessionDto;
import grsu.by.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payments")
@Slf4j
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/{orderId}")
    @ResponseStatus(HttpStatus.CREATED)
    public PaymentShortDto createPayment(@PathVariable Long orderId) {
        return paymentService.createPayment(orderId);
    }

    @PostMapping("/{paymentId}/stripe-session")
    @ResponseStatus(HttpStatus.CREATED)
    public StripeSessionDto createStripeSession(@PathVariable Long paymentId) {
        log.info("Creating Stripe session for payment {}", paymentId);
        return paymentService.createStripeSession(paymentId);
    }

    @PostMapping("/confirmation/{id}")
    @ResponseStatus(HttpStatus.OK)
    public boolean confirmPayment(@PathVariable Long id) {
        return paymentService.confirmPayment(id);
    }

    @PostMapping("/cancel/{id}")
    @ResponseStatus(HttpStatus.OK)
    public boolean cancelPayment(@PathVariable Long id) {
        return paymentService.cancelPayment(id);
    }

    @PostMapping("/stripe/webhook")
    public ResponseEntity<String> stripeWebhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader
    ) {
        return paymentService.handleStripeWebhook(payload, sigHeader)
                ? ResponseEntity.ok("OK")
                : ResponseEntity.badRequest().body("Webhook verification failed");
    }
}