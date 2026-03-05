package grsu.by.service;

import grsu.by.entity.Mail;

import java.util.concurrent.CompletableFuture;

public interface EmailSender {
    CompletableFuture<Mail> sendEmail(Mail mail);
}
