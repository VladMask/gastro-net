package grsu.by.scheduler;

import grsu.by.config.properties.NotificationServiceProperties;
import grsu.by.entity.Mail;
import grsu.by.enums.SendStatus;
import grsu.by.repository.MailRepository;
import grsu.by.service.EmailSender;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.data.domain.Limit;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
public class EmailScheduler {
    private final EmailSender emailSender;
    private final MailRepository mailRepository;
    private final int EXPIRATION_TIME;

    public EmailScheduler(
            EmailSender emailSender,
            MailRepository mailRepository,
            NotificationServiceProperties properties) {
        this.emailSender = emailSender;
        this.mailRepository = mailRepository;
        this.EXPIRATION_TIME = properties.getVerificationCode().getExpirationTimeSeconds();
    }

    @Transactional
    @Scheduled(
            initialDelayString = "${notification-service.scheduler.initial-delay}",
            fixedDelayString = "${notification-service.scheduler.fixed-delay}"
    )
    @SchedulerLock(name = "sendEmails")
    public void sendVerificationCodes() {
        List<Mail> mails = mailRepository.findByStatus(SendStatus.NEW, Limit.of(10));
        List<CompletableFuture<Mail>> futures = new ArrayList<>();
        mails.forEach(mail -> {
            if (mail.getCreatedAt().isAfter(Instant.now().minusSeconds(EXPIRATION_TIME))) {
                futures.add(emailSender.sendEmail(mail));
            } else {
                mail.setStatus(SendStatus.ERROR);
            }
        });
        futures.forEach(CompletableFuture::join);
        mailRepository.saveAll(mails);
    }


}
