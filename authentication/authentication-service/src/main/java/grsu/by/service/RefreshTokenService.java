package grsu.by.service;

import grsu.by.dto.RefreshTokensRequest;
import grsu.by.entity.RefreshToken;

public interface RefreshTokenService {
    RefreshToken createRefreshToken(String email);
    RefreshToken refreshToken(RefreshTokensRequest refreshTokenRequest);
    void deleteByToken(RefreshTokensRequest refreshTokenRequest);
    void deleteExpiredTokens();
}
