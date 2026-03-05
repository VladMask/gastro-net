package grsu.by.service.impl;

import grsu.by.config.properties.PaymentServiceProperties;
import grsu.by.dto.paymentDto.PaymentInvoiceDto;
import grsu.by.dto.paymentDto.PaymentShortDto;
import grsu.by.entity.Payment;
import grsu.by.enums.PaymentStatus;
import grsu.by.repository.PaymentRepository;
import grsu.by.service.BlackBoxService;
import grsu.by.service.PaymentService;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final ModelMapper mapper;
    private final BlackBoxService blackBoxService;

    private final BigDecimal PREPAYMENT_PERCENTAGE;

    public PaymentServiceImpl(
            PaymentRepository paymentRepository,
            ModelMapper mapper,
            BlackBoxService blackBoxService,
            PaymentServiceProperties properties) {
        this.paymentRepository = paymentRepository;
        this.mapper = mapper;
        this.blackBoxService = blackBoxService;
        this.PREPAYMENT_PERCENTAGE = properties.getService().getPrepaymentPercentage();
    }

    @Transactional
    @Override
    public PaymentShortDto savePayment(Payment payment) {
        if (paymentRepository.countByOrderId(payment.getOrderId()) > 1) {
            throw new IllegalStateException("Payment already made");
        }
        if (paymentRepository.existsByOrderId(payment.getOrderId())) {
            paymentRepository.save(payment);
        }
        else {
            payment.setAmount(
                    payment.getAmount().multiply(PREPAYMENT_PERCENTAGE)
                            .setScale(2, RoundingMode.HALF_UP)
            );
            payment = paymentRepository.save(payment);
        }
        return mapper.map(paymentRepository.save(payment), PaymentShortDto.class);
    }

    @Transactional
    @Override
    public boolean confirmPayment(Long id) {
        Payment payment = paymentRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Payment not found")
        );

        if (blackBoxService.confirmPayment()) {
            blackBoxService.createBankingPayment(payment, PaymentStatus.CONFIRMED);
            payment.setStatus(PaymentStatus.CONFIRMED);
            paymentRepository.save(payment);
            return true;
        } else {
            blackBoxService.createBankingPayment(payment, PaymentStatus.PENDING_CONFIRMATION);
            return false;
        }

    }

    @Override
    public PaymentInvoiceDto getInvoiceDataById(Long id) {
        Payment payment = paymentRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Payment not found")
        );
        return mapper.map(payment, PaymentInvoiceDto.class);
    }

}
