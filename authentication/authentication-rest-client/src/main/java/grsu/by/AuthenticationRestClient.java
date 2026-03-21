package grsu.by;

import grsu.by.config.properties.AuthenticationRestClientProperties;
import grsu.by.dto.AuthenticationResponse;
import grsu.by.dto.AuthenticationRequest;
import grsu.by.dto.RegistrationRequest;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

@Getter
@Setter
public class AuthenticationRestClient {
    private final AuthenticationRestClientProperties restClientProperties;
    private final RestClient restClient;

    public AuthenticationRestClient(AuthenticationRestClientProperties restClientProperties, RestClient.Builder restClientBuilder) {
        this.restClientProperties = restClientProperties;
        this.restClient = restClientBuilder
                .baseUrl(restClientProperties.getBaseUrl())
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public AuthenticationResponse login(AuthenticationRequest request) {
        return restClient
                .post()
                .uri("/api/v1/authentication/login")
                .body(request)
                .retrieve()
                .body(AuthenticationResponse.class);
    }

    public AuthenticationResponse register(RegistrationRequest request) {
        return restClient
                .post()
                .uri("/api/v1/authentication/register")
                .body(request)
                .retrieve()
                .body(AuthenticationResponse.class);
    }

    public AuthenticationResponse refreshTokens(String refreshToken) {
        return  restClient
                .post()
                .uri("/api/v1/authentication/refresh")
                .body(refreshToken)
                .retrieve()
                .body(AuthenticationResponse.class);
    }

    public String logout(String refreshToken) {
        return restClient
                .post()
                .uri("/api/v1/authentication/logout")
                .body(refreshToken)
                .retrieve()
                .body(String.class);
    }
}
