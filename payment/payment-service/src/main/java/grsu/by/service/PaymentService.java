package grsu.by.service;

import grsu.by.dto.paymentDto.PaymentInvoiceDto;
import grsu.by.dto.paymentDto.PaymentShortDto;
import grsu.by.entity.Payment;

public interface PaymentService {
    PaymentShortDto savePayment(Payment payment);
    boolean confirmPayment(Long id);
    PaymentInvoiceDto getInvoiceDataById(Long id);
}
