package grsu.by.repository;

import grsu.by.entity.Mail;
import grsu.by.enums.SendStatus;
import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MailRepository extends JpaRepository<Mail, Long> {
    List<Mail> findByStatus(SendStatus status, Limit limit);
}
