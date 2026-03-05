package grsu.by.service.impl;

import grsu.by.entity.Mail;
import grsu.by.enums.SendStatus;
import grsu.by.service.EmailSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailSenderImpl implements EmailSender {
    private final JavaMailSender mailSender;

    @Override
    @Async
    public CompletableFuture<Mail> sendEmail(Mail mail) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mail.getReceiverEmail());
        message.setSubject(mail.getSubject());
        message.setText(mail.getBody());
        try {
            mailSender.send(message);
            mail.setStatus(SendStatus.SUCCESS);
        } catch (MailException e) {
            log.error(e.getMessage());
        }
        return CompletableFuture.completedFuture(mail);
    }
}
