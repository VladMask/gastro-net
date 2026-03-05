package grsu.by.scheduler;

import grsu.by.entity.BankingPayment;
import grsu.by.enums.PaymentStatus;
import grsu.by.repository.BankingPaymentRepository;
import grsu.by.service.BlackBoxService;
import lombok.RequiredArgsConstructor;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.data.domain.Limit;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BankingPaymentScheduler {
    private final BankingPaymentRepository bankingPaymentRepository;
    private final BlackBoxService blackBoxService;

    @Transactional
    @Scheduled(
            initialDelayString = "${common.outbox.scheduler.initial-delay}",
            fixedDelayString = "${common.outbox.scheduler.fixed-delay}"
    )
    @SchedulerLock(name = "retryBankingPayments")
    public void retryBankingPayments() {
        List<BankingPayment> payments = bankingPaymentRepository.findByStatus(PaymentStatus.PENDING_CONFIRMATION, Limit.of(10));
        for (BankingPayment payment : payments) {
            if (payment.getCreatedAt().plusSeconds(60*60).isBefore(Instant.now())) {
                payment.setStatus(PaymentStatus.ERROR);
                blackBoxService.abortPayment(payment);
            }
            else {
                if (blackBoxService.confirmPayment()) {
                    payment.setStatus(PaymentStatus.CONFIRMED);
                }
            }
        }
        bankingPaymentRepository.saveAll(payments);
    }
}
