package grsu.by;

import grsu.by.config.properties.ReservationRestClientProperties;
import grsu.by.dto.reservationDto.ReservationFullDto;
import grsu.by.dto.restaurantTableDto.RestaurantTableFullDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

import java.util.List;

@Getter
@Setter
public class ReservationRestClient {
    private ReservationRestClientProperties properties;
    private RestClient restClient;

    public ReservationRestClient(ReservationRestClientProperties properties, RestClient.Builder restClientBuilder) {
        this.properties = properties;
        this.restClient = restClientBuilder
                .baseUrl(properties.getBaseUrl())
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public ReservationFullDto findReservationById(Long id) {
        return restClient
                .get()
                .uri("/api/v1/reservations/" + id)
                .retrieve()
                .body(ReservationFullDto.class);
    }

    public void cancelReservationById(Long id) {
        restClient
                .patch()
                .uri("/api/v1/reservations/{id}/cancel", id)
                .retrieve()
                .toBodilessEntity();
    }

    public List<ReservationFullDto> findReservationsByUserId(Long userId) {
        return restClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v1/reservations")
                        .queryParam("userId", userId)
                        .build())
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
    }

    public List<RestaurantTableFullDto> findTablesByRestaurantId(Long restaurantId) {
        return restClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v1/restaurants/tables")
                        .queryParam("restaurantId", restaurantId)
                        .build())
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
    }

    public List<RestaurantTableFullDto> findAvailableTablesByRestaurantId(Long restaurantId) {
        return restClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v1/restaurants/tables")
                        .queryParam("restaurantId", restaurantId)
                        .queryParam("status", "AVAILABLE")
                        .build())
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
    }

}
