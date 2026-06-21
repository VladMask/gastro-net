package grsu.by.service;

import grsu.by.entity.RefreshToken;

public interface RefreshTokenService {
    RefreshToken createRefreshToken(String email);
    RefreshToken refreshToken(String tokenToRefresh);
    void deleteByToken(String refreshToken);
    void deleteExpiredTokens();
}
