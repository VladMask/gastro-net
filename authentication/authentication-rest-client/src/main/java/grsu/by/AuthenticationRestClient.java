package grsu.by;

import grsu.by.config.properties.AuthenticationRestClientProperties;
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

    public void assignRole(Long profileId, String roleName) {
        restClient
                .post()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v1/authentication/internal/profiles/{profileId}/roles")
                        .queryParam("roleName", roleName)
                        .build(profileId))
                .retrieve()
                .toBodilessEntity();
    }
}
