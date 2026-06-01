package grsu.by;

import grsu.by.config.properties.RestaurantRestClientProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

public class RestaurantRestClient {
    private final RestClient restClient;

    public RestaurantRestClient(RestaurantRestClientProperties properties, RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder
                .baseUrl(properties.getBaseUrl())
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }


    public boolean isAdminOfRestaurant(Long profileId, Long restaurantId) {
        return Boolean.TRUE.equals(
                restClient
                        .get()
                        .uri("/api/v1/restaurants/{restaurantId}/admins/{profileId}",
                                restaurantId, profileId)
                        .retrieve()
                        .body(Boolean.class)
        );
    }
}
