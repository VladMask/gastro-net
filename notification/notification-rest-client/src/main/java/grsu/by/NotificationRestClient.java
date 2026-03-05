package grsu.by;

import grsu.by.config.properties.NotificationRestClientProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

public class NotificationRestClient {
    private final RestClient restClient;

    public NotificationRestClient(NotificationRestClientProperties properties, RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder
                .baseUrl(properties.getBaseUrl())
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public Boolean confirmVerificationCode(Integer verificationCode) {
        return restClient
                .post()
                .uri("/api/v1/notifications/confirmation/" + verificationCode)
                .retrieve()
                .body(Boolean.class);
    }
}
