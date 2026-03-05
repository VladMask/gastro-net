package grsu.by.controller;

import grsu.by.dto.paymentDto.PaymentInvoiceDto;
import grsu.by.dto.paymentDto.PaymentShortDto;
import grsu.by.patterns.templateMethod.impl.PaymentProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payments")
public class PaymentController {
    private final PaymentProcessor paymentProcessor;

    @PostMapping("/{orderId}")
    @ResponseStatus(HttpStatus.CREATED)
    public PaymentShortDto createPayment(@PathVariable Long orderId) {
        return paymentProcessor.makePayment(orderId);
    }

    @PostMapping("/prepayment/{orderId}")
    @ResponseStatus(HttpStatus.CREATED)
    public PaymentShortDto createPrepayment(@PathVariable Long orderId) {
        return paymentProcessor.makePayment(orderId);
    }

    @PostMapping("/confirmation/{id}")
    @ResponseStatus(HttpStatus.OK)
    public boolean confirmPayment(@PathVariable Long id) {
        return paymentProcessor.confirmPayment(id);
    }

    @GetMapping("invoices/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PaymentInvoiceDto getInvoiceDataById(@PathVariable Long id) {
        return paymentProcessor.getInvoiceDataById(id);
    }
}
