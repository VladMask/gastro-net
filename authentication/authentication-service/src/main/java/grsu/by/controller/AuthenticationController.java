package grsu.by.controller;

import grsu.by.dto.AuthenticationRequest;
import grsu.by.dto.AuthenticationResponse;
import grsu.by.dto.RegistrationRequest;
import grsu.by.service.AuthenticationService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;


@RestController
@RequestMapping("/api/v1/authentication")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authService;

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public void login(
            @RequestBody @Valid AuthenticationRequest request,
            HttpServletResponse response
    ) {
        AuthenticationResponse tokens = authService.login(request);
        addTokenCookies(response, tokens);
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.OK)
    public void register(
            @RequestBody @Valid RegistrationRequest request,
            HttpServletResponse response
    ) {
        AuthenticationResponse tokens = authService.register(request);
        addTokenCookies(response, tokens);
    }

    @PostMapping("/refresh")
    @ResponseStatus(HttpStatus.OK)
    public void refresh(
            @CookieValue(name = "refresh_token") String refreshToken,
            HttpServletResponse response
    ) {
        AuthenticationResponse tokens = authService.refreshTokens(refreshToken);
        addTokenCookies(response, tokens);
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.OK)
    public void logout(
            @CookieValue(name = "refresh_token") String refreshToken,
            HttpServletResponse response) {
        authService.logout(refreshToken);
        clearTokenCookies(response);
    }

    private void addTokenCookies(HttpServletResponse response, AuthenticationResponse tokens) {
        ResponseCookie accessCookie = ResponseCookie.from("access_token", tokens.getAccessToken())
                .httpOnly(true)
                .secure(false)
                .sameSite("Strict")
                .path("/")
                .maxAge(Duration.ofMinutes(15))
                .build();

        ResponseCookie refreshCookie = ResponseCookie.from("refresh_token", tokens.getRefreshToken())
                .httpOnly(true)
                .secure(false)
                .sameSite("Strict")
                .path("/api/v1/authentication/refresh")
                .maxAge(Duration.ofDays(7))
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());
    }

    private void clearTokenCookies(HttpServletResponse response) {
        ResponseCookie accessCookie = ResponseCookie.from("access_token", "")
                .httpOnly(true)
                .path("/")
                .maxAge(0)
                .build();

        ResponseCookie refreshCookie = ResponseCookie.from("refresh_token", "")
                .httpOnly(true)
                .path("/api/v1/authentication/refresh")
                .maxAge(0)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());
    }
}
