package grsu.by.repository;

import grsu.by.entity.BankingPayment;
import grsu.by.enums.PaymentStatus;
import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BankingPaymentRepository extends JpaRepository<BankingPayment, Long> {
    List<BankingPayment> findByStatus(PaymentStatus status);

    List<BankingPayment> findByStatus(PaymentStatus status, Limit limit);
}
