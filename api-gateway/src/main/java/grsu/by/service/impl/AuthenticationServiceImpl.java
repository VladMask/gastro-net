package grsu.by.service.impl;

import grsu.by.AuthenticationRestClient;
import grsu.by.UserRestClient;
import grsu.by.dto.AuthenticationRequest;
import grsu.by.dto.AuthenticationResponse;
import grsu.by.dto.RefreshTokensRequest;
import grsu.by.dto.UserCreationDto;
import grsu.by.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRestClient userRestClient;
    private final AuthenticationRestClient authenticationRestClient;

    @Override
    public AuthenticationResponse login(AuthenticationRequest request) {
        return authenticationRestClient.login(request);
    }

    @Override
    public AuthenticationResponse register(UserCreationDto creationDto) {
        userRestClient.create(creationDto);
        return authenticationRestClient.register(
                AuthenticationRequest.builder()
                        .email(creationDto.getEmail())
                        .password(creationDto.getPassword())
                        .build()
        );
    }

    @Override
    public AuthenticationResponse refreshTokens(RefreshTokensRequest refreshTokensRequest) {
        return authenticationRestClient.refreshTokens(refreshTokensRequest);
    }

    @Override
    public String logout(RefreshTokensRequest refreshTokensRequest) {
        return authenticationRestClient.logout(refreshTokensRequest);
    }
}
