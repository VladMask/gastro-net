package grsu.by.controller;

import grsu.by.dto.AuthenticationResponse;
import grsu.by.dto.AuthenticationRequest;
import grsu.by.dto.RefreshTokensRequest;
import grsu.by.service.ProfileService;
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
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class ProfileController {
    private final ProfileService service;

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public AuthenticationResponse login(@RequestBody @Valid AuthenticationRequest request) {
        log.info("Login request received");
        return service.login(request);
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.OK)
    public void logout(@RequestBody @Valid RefreshTokensRequest refreshToken) {
        log.info("Logout request received");
        service.logout(refreshToken);
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.OK)
    public AuthenticationResponse register(@RequestBody @Valid AuthenticationRequest request) {
        log.info("Register request received");
        return service.register(request);
    }

    @PostMapping("/refresh")
    @ResponseStatus(HttpStatus.OK)
    public AuthenticationResponse refresh(@RequestBody @Valid RefreshTokensRequest refreshTokenRequest) {
        log.info("Refresh request received");
        return service.refreshTokens(refreshTokenRequest);
    }
}
