package grsu.by.service.impl;

import grsu.by.dto.AuthenticationResponse;
import grsu.by.dto.AuthenticationRequest;
import grsu.by.dto.RefreshTokensRequest;
import grsu.by.entity.Profile;
import grsu.by.entity.RefreshToken;
import grsu.by.entity.Role;
import grsu.by.jwt.JwtGenerator;
import grsu.by.repository.ProfileRepository;
import grsu.by.repository.RoleRepository;
import grsu.by.service.ProfileService;
import grsu.by.service.RefreshTokenService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.AuthenticationException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {
    private final ProfileRepository profileRepository;
    private final RoleRepository roleRepository;
    private final ModelMapper mapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtGenerator jwtGenerator;
    private final RefreshTokenService refreshTokenService;
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
    public AuthenticationResponse register(AuthenticationRequest request) {
        Profile profile = mapper.map(request, Profile.class);
        profile.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        Role role = roleRepository.findByName(DEFAULT_ROLE_NAME).orElseThrow(
                () -> new EntityNotFoundException("Role not found")
        );
        profile.setRoles(List.of(role));
        profileRepository.save(profile);

        return getTokenPair(profile.getEmail());
    }

    @Transactional
    @Override
    public AuthenticationResponse refreshTokens(RefreshTokensRequest refreshToken) {
        RefreshToken refreshedToken = refreshTokenService.refreshToken(refreshToken);
        Profile profile = profileRepository.findById(refreshedToken.getProfileId()).orElseThrow(
                () -> new EntityNotFoundException("Profile not found")
        );
        String accessToken = jwtGenerator.generate(profile.getEmail());
        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshedToken.getToken())
                .build();
    }

    @Override
    public void logout(RefreshTokensRequest refreshTokenRequest) {
        refreshTokenService.deleteByToken(refreshTokenRequest);
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
