package grsu.by;

import grsu.by.config.properties.UserRestClientProperties;
import grsu.by.dto.EmailResponse;
import grsu.by.dto.UserCreationDto;
import grsu.by.dto.UserShortDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

@Getter
@Setter
public class UserRestClient {
    private final UserRestClientProperties restClientProperties;
    private final RestClient restClient;

    public UserRestClient(UserRestClientProperties restClientProperties, RestClient.Builder restClientBuilder) {
        this.restClientProperties = restClientProperties;
        this.restClient = restClientBuilder
                .baseUrl(restClientProperties.getBaseUrl())
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public Long create(UserCreationDto creationDto) {
        return restClient
                .post()
                .uri("/api/v1/users")
                .body(creationDto)
                .retrieve()
                .body(Long.class);
    }

    public UserShortDto findById(Long userId) {
        return restClient
                .get()
                .uri("/api/v1/users/" + userId)
                .retrieve()
                .body(UserShortDto.class);
    }

    public EmailResponse getEmailByUserId(Long userId) {
        return restClient
                .get()
                .uri("/api/v1/users/email/" + userId)
                .retrieve()
                .body(EmailResponse.class);
    }
}
