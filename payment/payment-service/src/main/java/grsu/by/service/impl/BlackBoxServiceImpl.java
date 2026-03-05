package grsu.by.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import grsu.by.entity.BankingPayment;
import grsu.by.entity.OutboxEvent;
import grsu.by.entity.Payment;
import grsu.by.enums.PaymentStatus;
import grsu.by.repository.BankingPaymentRepository;
import grsu.by.service.BlackBoxService;
import grsu.by.service.OutboxEventService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class BlackBoxServiceImpl implements BlackBoxService {
    private final BankingPaymentRepository bankingPaymentRepository;
    private final OutboxEventService outboxEventService;
    private final ObjectMapper objectMapper;
    private final Random random = new Random();

    @SneakyThrows
    @Transactional
    @Override
    public void createBankingPayment(Payment payment, PaymentStatus status) {
        BankingPayment bankingPayment = BankingPayment.builder()
                .userId(payment.getUserId())
                .amount(payment.getAmount())
                .restaurantId(payment.getRestaurantId())
                .status(status)
                .build();
        outboxEventService.create(
                new OutboxEvent(
                    "Banking payment created",
                    objectMapper.writeValueAsString(payment)
                )
        );
        bankingPaymentRepository.save(bankingPayment);
    }

    @Override
    public boolean confirmPayment() {
        return random.nextBoolean();
    }

    @Transactional
    @SneakyThrows
    @Override
    public void abortPayment(BankingPayment bankingPayment) {
        bankingPayment.setStatus(PaymentStatus.ERROR);
        outboxEventService.create(new OutboxEvent(
                "Banking payment aborted",
                objectMapper.writeValueAsString(bankingPayment)
        ));
    }
}
