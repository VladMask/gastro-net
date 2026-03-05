package grsu.by.controller;

import grsu.by.dto.paymentDto.PaymentShortDto;
import grsu.by.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api-gateway/v1/payments")
public class PaymentController {
    private final PaymentService service;

    @PostMapping("/{orderId}")
    @ResponseStatus(HttpStatus.CREATED)
    public PaymentShortDto createPayment(@PathVariable Long orderId) {
        return service.createPayment(orderId);
    }

    @PostMapping("/prepayment/{orderId}")
    @ResponseStatus(HttpStatus.CREATED)
    public PaymentShortDto createPrepayment(@PathVariable Long orderId) {
        return service.createPrepayment(orderId);
    }

    @PostMapping("/confirmation/{verificationCode}/{paymentId}")
    @ResponseStatus(HttpStatus.OK)
    public boolean confirmPayment(@PathVariable Integer verificationCode, @PathVariable Long paymentId) {
        return service.confirmPayment(verificationCode, paymentId);
    }
}
