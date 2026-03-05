package grsu.by.service;

import grsu.by.dto.AuthenticationRequest;
import grsu.by.dto.AuthenticationResponse;
import grsu.by.dto.RefreshTokensRequest;
import grsu.by.dto.UserCreationDto;

public interface AuthenticationService {
    AuthenticationResponse login(AuthenticationRequest request);
    AuthenticationResponse register(UserCreationDto creationDto);
    AuthenticationResponse refreshTokens(RefreshTokensRequest refreshTokensRequest);
    String logout(RefreshTokensRequest refreshTokensRequest);
}
