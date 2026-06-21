package grsu.by.service;

import grsu.by.dto.paymentDto.PaymentShortDto;
import grsu.by.dto.paymentDto.StripeSessionDto;

public interface PaymentService {
    PaymentShortDto createPayment(Long orderId);
    StripeSessionDto createStripeSession(Long paymentId);
    boolean confirmPayment(Long id);
    boolean cancelPayment(Long id);
    boolean handleStripeWebhook(String payload, String sigHeader);
}
