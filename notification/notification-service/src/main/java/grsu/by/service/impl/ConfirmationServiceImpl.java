package grsu.by.service.impl;

import grsu.by.UserRestClient;
import grsu.by.config.properties.NotificationServiceProperties;
import grsu.by.dto.PaymentEventDto;
import grsu.by.entity.Mail;
import grsu.by.entity.VerificationCode;
import grsu.by.enums.SendStatus;
import grsu.by.repository.MailRepository;
import grsu.by.repository.VerificationCodeRepository;
import grsu.by.service.ConfirmationService;
import grsu.by.service.EmailSender;
import grsu.by.utils.CodeHasher;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Random;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletionException;

@Slf4j
@Service
public class ConfirmationServiceImpl implements ConfirmationService {
    private final UserRestClient userRestClient;
    private final VerificationCodeRepository verificationCodeRepository;
    private final MailRepository mailRepository;
    private final EmailSender emailSender;
    private final Random random;
    private final CodeHasher codeHasher;
    private final int EXPIRATION_TIME;
    private static final String MAIL_SUBJECT = "Payment verification";
    private static final String MAIL_BODY_PREFIX = "Your verification code is: ";

    public ConfirmationServiceImpl(
            UserRestClient userRestClient,
            VerificationCodeRepository verificationCodeRepository,
            MailRepository mailRepository,
            EmailSender emailSender,
            CodeHasher codeHasher,
            NotificationServiceProperties properties) {
        this.userRestClient = userRestClient;
        this.verificationCodeRepository = verificationCodeRepository;
        this.mailRepository = mailRepository;
        this.emailSender = emailSender;
        this.codeHasher = codeHasher;
        this.random = new Random();
        this.EXPIRATION_TIME = properties.getVerificationCode().getExpirationTimeSeconds();
    }

    @Transactional
    @Override
    public void sendVerificationCode(PaymentEventDto dto) {
        int code = random.nextInt(99_999, 999_999);

        VerificationCode verificationCode = VerificationCode.builder()
                .codeHash(codeHasher.hash(String.valueOf(code)))
                .receiverEmail(userRestClient.getEmailByUserId(dto.getUserId()).getEmail())
                .createdAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(EXPIRATION_TIME))
                .build();
        verificationCodeRepository.save(verificationCode);

        Mail mail = Mail.builder()
                .receiverEmail(verificationCode.getReceiverEmail())
                .subject(MAIL_SUBJECT)
                .body(MAIL_BODY_PREFIX + code)
                .status(SendStatus.NEW)
                .build();
        mailRepository.save(mail);

        try {
            mailRepository.save(emailSender.sendEmail(mail).join());
        }
        catch (CompletionException | CancellationException e) {
            log.error("{} during email sends in {}", e.getClass().getName(), this.getClass().getName(), e);
        }

    }

    @Override
    public boolean confirmVerificationCode(Integer verificationCode) {
        VerificationCode code = verificationCodeRepository.findVerificationCodeByCodeHash(
                codeHasher.hash(String.valueOf(verificationCode))
                )
                .orElseThrow(
                        () -> new EntityNotFoundException("Specified verification code does not exist")
                );
        if (code.getExpiresAt().isBefore(Instant.now())) {
            throw new IllegalStateException("Verification code expired");
        }
        return true;
    }
}
