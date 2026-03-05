package grsu.by.service.impl;

import grsu.by.dto.RefreshTokensRequest;
import grsu.by.entity.Profile;
import grsu.by.entity.RefreshToken;
import grsu.by.repository.ProfileRepository;
import grsu.by.repository.RefreshTokenRepository;
import grsu.by.service.RefreshTokenService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final ProfileRepository profileRepository;
    @Value("${jwt.refresh-token.expiration-time}")
    private Long REFRESH_TOKEN_EXPIRATION_TIME;

    @Transactional
    @Override
    public RefreshToken createRefreshToken(String email) {
        Profile profile = profileRepository.findByEmail(email).orElseThrow(
                () -> new EntityNotFoundException("Profile with email " + email + " not found")
        );
        RefreshToken refreshToken = RefreshToken.builder()
                .profileId(profile.getId())
                .token(UUID.randomUUID().toString())
                .expirationDate((new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION_TIME).toInstant()))
                .build();
        refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    @Transactional
    @Override
    public RefreshToken refreshToken(RefreshTokensRequest refreshTokenRequest) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(refreshTokenRequest.getRefreshToken()).orElseThrow(
                () -> new EntityNotFoundException("Token not found")
        );
        if (refreshToken.getExpirationDate().isBefore(Instant.now())) {
            throw new IllegalStateException("Token is expired");
        }
        RefreshToken refreshedToken = RefreshToken.builder()
                .profileId(refreshToken.getProfileId())
                .token(UUID.randomUUID().toString())
                .expirationDate((new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION_TIME).toInstant()))
                .build();
        return refreshTokenRepository.save(refreshedToken);
    }

    @Transactional
    @Override
    public void deleteByToken(RefreshTokensRequest refreshTokenRequest) {
        refreshTokenRepository.deleteByToken(refreshTokenRequest.getRefreshToken());
    }

    @Transactional
    @Override
    public void deleteExpiredTokens() {
        refreshTokenRepository.deleteByExpirationDateBefore(Instant.now());
    }
}
