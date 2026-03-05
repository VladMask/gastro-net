package grsu.by.repository;

import grsu.by.entity.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Long> {
    Optional<VerificationCode> findVerificationCodeByCodeHash(String codeHash);
}
