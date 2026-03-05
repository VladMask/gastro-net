package grsu.by.service;

import grsu.by.dto.PaymentEventDto;

public interface ConfirmationService {
    void sendVerificationCode(PaymentEventDto dto);
    boolean confirmVerificationCode(Integer verificationCode);
}
