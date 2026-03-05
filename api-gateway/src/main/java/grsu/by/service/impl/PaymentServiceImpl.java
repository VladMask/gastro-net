package grsu.by.service.impl;

import grsu.by.NotificationRestClient;
import grsu.by.PaymentRestClient;
import grsu.by.dto.paymentDto.PaymentShortDto;
import grsu.by.service.PaymentService;
import grsu.by.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRestClient paymentRestClient;
    private final ReservationService reservationService;
    private final NotificationRestClient notificationRestClient;

    @Override
    public PaymentShortDto createPayment(Long orderId) {
        return paymentRestClient.createPayment(orderId);
    }

    @Override
    public PaymentShortDto createPrepayment(Long orderId) {
        PaymentShortDto prepayment = paymentRestClient.createOrderPrepayment(orderId);
        reservationService.confirmReservationByOrderId(orderId);
        return prepayment;
    }

    @Override
    public boolean confirmPayment(Integer verificationCode, Long paymentId) {
        notificationRestClient.confirmVerificationCode(verificationCode);
        return paymentRestClient.confirmPayment(paymentId);
    }
}
