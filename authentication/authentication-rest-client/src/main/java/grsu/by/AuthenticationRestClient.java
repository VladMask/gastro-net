package grsu.by;

import grsu.by.config.properties.AuthenticationRestClientProperties;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;

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
        HttpServletRequest incoming = ((ServletRequestAttributes)
                Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();

        restClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v1/authentication/internal/profiles/{profileId}/roles")
                        .queryParam("roleName", roleName)
                        .build(profileId))
                .header("X-Auth-Login",      incoming.getHeader("X-Auth-Login"))
                .header("X-Auth-Profile-Id", incoming.getHeader("X-Auth-Profile-Id"))
                .header("X-Auth-Roles",      incoming.getHeader("X-Auth-Roles"))
                .retrieve()
                .toBodilessEntity();
    }
}
