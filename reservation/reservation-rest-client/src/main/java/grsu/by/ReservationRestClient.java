package grsu.by;

import grsu.by.config.properties.ReservationRestClientProperties;
import grsu.by.dto.reservationDto.ReservationCreationDto;
import grsu.by.dto.reservationDto.ReservationFullDto;
import grsu.by.dto.restaurantTableDto.RestaurantTableCreationDto;
import grsu.by.dto.restaurantTableDto.RestaurantTableFullDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

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

    public RestaurantTableFullDto createRestaurantTable(RestaurantTableCreationDto creationDto) {
        return restClient
                .post()
                .uri("/api/v1/restaurants/tables")
                .body(creationDto)
                .retrieve()
                .body(RestaurantTableFullDto.class);
    }
    public RestaurantTableFullDto findRestaurantTableById(Long id) {
        return restClient
                .get()
                .uri("/api/v1/restaurants/tables/" + id)
                .retrieve()
                .body(RestaurantTableFullDto.class);
    }

    public ReservationFullDto createReservation(ReservationCreationDto creationDto) {
        return restClient
                .post()
                .uri("/api/v1/reservations")
                .body(creationDto)
                .retrieve()
                .body(ReservationFullDto.class);
    }

    public ReservationFullDto findReservationById(Long id) {
        return restClient
                .get()
                .uri("/api/v1/reservations/" + id)
                .retrieve()
                .body(ReservationFullDto.class);
    }

    public void confirmReservationById(Long id){
        restClient
                .patch()
                .uri("api/v1/reservations/" + id);
    }

}
