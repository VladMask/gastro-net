package grsu.by.service;

import grsu.by.dto.AuthenticationResponse;
import grsu.by.dto.AuthenticationRequest;
import grsu.by.dto.RefreshTokensRequest;

public interface ProfileService {
    AuthenticationResponse login(AuthenticationRequest request);
    AuthenticationResponse register(AuthenticationRequest request);
    AuthenticationResponse refreshTokens(RefreshTokensRequest refreshTokenRequest);
    void logout(RefreshTokensRequest refreshTokenRequest);
}
