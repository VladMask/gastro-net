package grsu.by;

import grsu.by.config.properties.OrderRestClientProperties;
import grsu.by.dto.orderDto.OrderShortDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

@Getter
@Setter
public class OrderRestClient {
    private OrderRestClientProperties properties;
    private RestClient restClient;

    public OrderRestClient(OrderRestClientProperties properties, RestClient.Builder restClientBuilder) {
        this.properties = properties;
        this.restClient = restClientBuilder
                .baseUrl(properties.getBaseUrl())
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public OrderShortDto findOrderById(Long orderId) {
        return restClient
                .get()
                .uri("/api/v1/orders/" + orderId)
                .retrieve()
                .body(OrderShortDto.class);
    }

}
