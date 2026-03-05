package grsu.by.service;

import grsu.by.dto.paymentDto.PaymentShortDto;

public interface PaymentService {
    PaymentShortDto createPayment(Long orderId);
    PaymentShortDto createPrepayment(Long orderId);
    boolean confirmPayment(Integer verificationCode, Long paymentId);
}
