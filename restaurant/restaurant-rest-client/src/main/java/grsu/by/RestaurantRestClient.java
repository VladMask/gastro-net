package grsu.by;

import grsu.by.config.properties.RestaurantRestClientProperties;
import grsu.by.dto.PagedDataDto;
import grsu.by.dto.mealDto.MealInvoiceInfo;
import grsu.by.dto.restaurantDto.RestaurantCreationDto;
import grsu.by.dto.restaurantDto.RestaurantShortDto;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

import java.util.List;

public class RestaurantRestClient {
    private final RestClient restClient;

    public RestaurantRestClient(RestaurantRestClientProperties properties, RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder
                .baseUrl(properties.getBaseUrl())
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public RestaurantShortDto createRestaurant(RestaurantCreationDto creationDto) {
        return restClient
                .post()
                .uri("/api/v1/restaurants")
                .body(creationDto)
                .retrieve()
                .body(RestaurantShortDto.class);
    }


    public RestaurantShortDto findRestaurantById(Long id) {
        return restClient
                .get()
                .uri("/api/v1/restaurants/" + id)
                .retrieve()
                .body(RestaurantShortDto.class);
    }

    public PagedDataDto<RestaurantShortDto> findAllRestaurants(Integer page, Integer size) {
        return restClient
                .get()
                .uri(uriBuilder ->
                        uriBuilder.path("/api/v1/restaurants")
                        .queryParam("page", page)
                        .queryParam("size", size)
                        .build()
                )
                .retrieve()
                .body((new ParameterizedTypeReference<>() {}));
    }

    public List<MealInvoiceInfo> findMealsInfoByIds(List<Long> mealsIds) {
        return restClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v1/meals/info")
                        .queryParam("ids", mealsIds)
                        .build())
                .retrieve()
                .body((new ParameterizedTypeReference<>() {}));
    }
}
