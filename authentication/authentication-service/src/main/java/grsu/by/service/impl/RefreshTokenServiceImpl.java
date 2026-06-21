package grsu.by.service.impl;

import grsu.by.config.properties.AuthenticationServiceProperties;
import grsu.by.entity.Profile;
import grsu.by.entity.RefreshToken;
import grsu.by.repository.ProfileRepository;
import grsu.by.repository.RefreshTokenRepository;
import grsu.by.service.RefreshTokenService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final ProfileRepository profileRepository;
    private final Long REFRESH_TOKEN_EXPIRATION_TIME;

    public RefreshTokenServiceImpl(
            RefreshTokenRepository refreshTokenRepository,
            ProfileRepository profileRepository,
            AuthenticationServiceProperties properties) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.profileRepository = profileRepository;
        this.REFRESH_TOKEN_EXPIRATION_TIME = properties.getRefreshTokenService().getRefreshTokenExpirationTime();
    }

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
    public RefreshToken refreshToken(String tokenToRefresh) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(tokenToRefresh).orElseThrow(
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
    public void deleteByToken(String refreshToken) {
        refreshTokenRepository.deleteByToken(refreshToken);
    }

    @Transactional
    @Override
    public void deleteExpiredTokens() {
        refreshTokenRepository.deleteByExpirationDateBefore(Instant.now());
    }
}
