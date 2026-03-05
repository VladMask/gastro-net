package grsu.by.service;

import grsu.by.entity.BankingPayment;
import grsu.by.entity.Payment;
import grsu.by.enums.PaymentStatus;

public interface BlackBoxService {
    void createBankingPayment(Payment payment, PaymentStatus status);
    boolean confirmPayment();
    void abortPayment(BankingPayment bankingPayment);
}
