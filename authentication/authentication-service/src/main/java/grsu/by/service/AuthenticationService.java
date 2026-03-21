package grsu.by.service;

import grsu.by.dto.AuthenticationResponse;
import grsu.by.dto.AuthenticationRequest;

public interface AuthenticationService {
    AuthenticationResponse login(AuthenticationRequest request);
    AuthenticationResponse register(AuthenticationRequest request);
    AuthenticationResponse refreshTokens(String refreshToken);
    void logout(String refreshToken);
}
