package grsu.by.controller;

import grsu.by.dto.AuthenticationRequest;
import grsu.by.dto.AuthenticationResponse;
import grsu.by.dto.RefreshTokensRequest;
import grsu.by.dto.UserCreationDto;
import grsu.by.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api-gateway/v1/auth")
@Slf4j
public class AuthenticationController {
    private final AuthenticationService service;

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public AuthenticationResponse login(@RequestBody @Valid AuthenticationRequest request) {
        log.info("Login request received");
        return service.login(request);
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.OK)
    public String logout(@RequestBody @Valid RefreshTokensRequest refreshTokensRequest) {
        log.info("Logout request received");
        return service.logout(refreshTokensRequest);
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.OK)
    public AuthenticationResponse register(@RequestBody @Valid UserCreationDto creationDto) {
        log.info("Register request received");
        return service.register(creationDto);
    }

    @PostMapping("/refresh")
    @ResponseStatus(HttpStatus.OK)
    public AuthenticationResponse refresh(@RequestBody @Valid RefreshTokensRequest refreshTokensRequest) {
        log.info("Refresh request received");
        return service.refreshTokens(refreshTokensRequest);
    }
}
