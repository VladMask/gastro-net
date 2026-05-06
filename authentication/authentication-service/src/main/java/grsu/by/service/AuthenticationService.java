package grsu.by.service;

import grsu.by.dto.AuthenticationRequest;
import grsu.by.dto.AuthenticationResponse;
import grsu.by.dto.RegistrationRequest;
import grsu.by.dto.RolesResponseDto;

public interface AuthenticationService {
    AuthenticationResponse login(AuthenticationRequest request);
    AuthenticationResponse register(RegistrationRequest request);
    AuthenticationResponse refreshTokens(String refreshToken);
    void logout(String refreshToken);
    void assignRole(Long profileId, String roleName);
    RolesResponseDto getMyRoles(String email);
}
