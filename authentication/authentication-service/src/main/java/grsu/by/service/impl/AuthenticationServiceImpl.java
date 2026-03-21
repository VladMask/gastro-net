package grsu.by.service.impl;

import grsu.by.UserRestClient;
import grsu.by.dto.AuthenticationResponse;
import grsu.by.dto.AuthenticationRequest;
import grsu.by.dto.RegistrationRequest;
import grsu.by.dto.UserCreationDto;
import grsu.by.entity.Profile;
import grsu.by.entity.RefreshToken;
import grsu.by.entity.Role;
import grsu.by.jwt.JwtGenerator;
import grsu.by.repository.ProfileRepository;
import grsu.by.repository.RoleRepository;
import grsu.by.service.AuthenticationService;
import grsu.by.service.RefreshTokenService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.AuthenticationException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final ProfileRepository profileRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtGenerator jwtGenerator;
    private final RefreshTokenService refreshTokenService;
    private final UserRestClient userRestClient;
    private final String DEFAULT_ROLE_NAME = "USER";

    @Transactional
    @SneakyThrows
    @Override
    public AuthenticationResponse login(AuthenticationRequest request) {
        Profile profile = profileRepository.findByEmail(request.getEmail()).orElseThrow(
                () -> new EntityNotFoundException("Profile not found")
        );

        if(!passwordEncoder.matches(request.getPassword(), profile.getPasswordHash())) {
            throw new AuthenticationException("Wrong login or password");
        }

        return getTokenPair(request.getEmail());
    }

    @Transactional
    @Override
    public AuthenticationResponse register(RegistrationRequest request) {

        try {
            UserCreationDto userDto = UserCreationDto.builder()
                    .firstname(request.getFirstname())
                    .lastname(request.getLastname())
                    .email(request.getEmail())
                    .birthDate(request.getBirthDate())
                    .build();

            userRestClient.create(userDto);

        } catch (Exception ex) {
            throw new IllegalStateException("Failed to create user profile", ex);
        }
        Profile profile = createProfile(request);

        return getTokenPair(profile.getEmail());
    }

    private Profile createProfile(RegistrationRequest request) {
        Profile profile = Profile.builder()
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .build();
        Role role = roleRepository.findByName("USER")
                .orElseThrow(() -> new EntityNotFoundException("Role not found"));
        profile.setRoles(List.of(role));
        return profileRepository.save(profile);
    }

    @Transactional
    @Override
    public AuthenticationResponse refreshTokens(String refreshToken) {
        RefreshToken refreshedToken = refreshTokenService.refreshToken(refreshToken);
        Profile profile = profileRepository.findById(refreshedToken.getProfileId()).orElseThrow(
                () -> new EntityNotFoundException("Profile not found")
        );
        String accessToken = jwtGenerator.generate(profile.getEmail());

        refreshTokenService.deleteByToken(refreshToken);
        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshedToken.getToken())
                .build();
    }

    @Override
    public void logout(String refreshToken) {
        refreshTokenService.deleteByToken(refreshToken);
    }

    private AuthenticationResponse getTokenPair(String email) {
        String accessToken = jwtGenerator.generate(email);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(email);
        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getToken())
                .build();
    }
}
