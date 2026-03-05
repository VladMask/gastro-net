package grsu.by.service.impl;

import grsu.by.UserRestClient;
import grsu.by.config.properties.NotificationServiceProperties;
import grsu.by.dto.EmailResponse;
import grsu.by.dto.PaymentEventDto;
import grsu.by.entity.Mail;
import grsu.by.entity.VerificationCode;
import grsu.by.enums.SendStatus;
import grsu.by.repository.MailRepository;
import grsu.by.repository.VerificationCodeRepository;
import grsu.by.service.EmailSender;
import grsu.by.utils.CodeHasher;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.CompletableFuture;

@ExtendWith(MockitoExtension.class)
class ConfirmationServiceImplTest {
    
    private final UserRestClient userRestClient;
    private final VerificationCodeRepository verificationCodeRepository;
    private final MailRepository mailRepository;
    private final EmailSender emailSender;
    private final ConfirmationServiceImpl confirmationService;
    @Captor
    ArgumentCaptor<Mail> mailArgumentCaptor;

    public ConfirmationServiceImplTest() {
        this.userRestClient = Mockito.mock(UserRestClient.class);
        this.verificationCodeRepository = Mockito.mock(VerificationCodeRepository.class);
        this.mailRepository = Mockito.mock(MailRepository.class);
        this.emailSender = Mockito.mock(EmailSender.class);
        NotificationServiceProperties properties = getNotificationServiceProperties();
        CodeHasher hasher = new CodeHasher(properties);
        this.confirmationService = new ConfirmationServiceImpl(
                userRestClient,
                verificationCodeRepository,
                mailRepository,
                emailSender,
                hasher,
                properties
        );
    }

    @NotNull
    private static NotificationServiceProperties getNotificationServiceProperties() {
        NotificationServiceProperties properties = new NotificationServiceProperties();
        NotificationServiceProperties.VerificationCode verificationCode = new NotificationServiceProperties.VerificationCode();
        NotificationServiceProperties.CodeHasher codeHasher = new NotificationServiceProperties.CodeHasher();
        codeHasher.setOtpSecret("secret");
        verificationCode.setExpirationTimeSeconds(90);
        properties.setVerificationCode(verificationCode);
        properties.setCodeHasher(codeHasher);
        return properties;
    }

    @Test
    void sendVerificationCode() {
        Mockito.when(userRestClient.getEmailByUserId(Mockito.anyLong()))
                .thenReturn( new EmailResponse("some@email"));
        Mockito.when(verificationCodeRepository.save(Mockito.any(VerificationCode.class)))
                .thenReturn(
                        VerificationCode.builder()
                                .receiverEmail("some@email")
                                .codeHash("123456")
                                .build()
                );
        Mockito.when(mailRepository.save(Mockito.any(Mail.class)))
                .thenReturn(
                        Mail.builder()
                                .subject("Verification Code")
                                .body("Verification Code")
                                .status(SendStatus.NEW)
                                .receiverEmail("some@email")
                                .build()
                );
        Mockito.when(emailSender.sendEmail(Mockito.any(Mail.class)))
                .thenReturn(CompletableFuture.failedFuture(new RuntimeException()));

        confirmationService.sendVerificationCode(new PaymentEventDto(1L));

        Mockito.verify(verificationCodeRepository, Mockito.times(1))
                .save(Mockito.any());

        Mockito.verify(mailRepository, Mockito.times(1))
                .save(mailArgumentCaptor.capture());
        Mail mailBeforeSend = mailArgumentCaptor.getValue();

        Mockito.verify(emailSender, Mockito.times(1))
                .sendEmail(mailArgumentCaptor.capture());
        Mail mailAfterSend = mailArgumentCaptor.getValue();

        Assertions.assertEquals(SendStatus.NEW, mailBeforeSend.getStatus());
        Assertions.assertEquals(SendStatus.NEW, mailAfterSend.getStatus());
    }
}