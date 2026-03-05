package grsu.by;

import grsu.by.config.properties.PaymentRestClientProperties;
import grsu.by.dto.paymentDto.PaymentInvoiceDto;
import grsu.by.dto.paymentDto.PaymentShortDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

public class PaymentRestClient {
    private final RestClient restClient;

    public PaymentRestClient(PaymentRestClientProperties properties, RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder
                .baseUrl(properties.getBaseUrl())
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public PaymentShortDto createPayment(Long orderId) {
        return restClient
                .post()
                .uri("/api/v1/payments/" + orderId)
                .retrieve()
                .body(PaymentShortDto.class);
    }

    public PaymentShortDto createOrderPrepayment(Long orderId) {
        return restClient
                .post()
                .uri("/api/v1/payments/prepayment/" + orderId)
                .retrieve()
                .body(PaymentShortDto.class);
    }

    public Boolean confirmPayment(Long paymentId) {
        return restClient
                .post()
                .uri("/api/v1/payments/confirmation/" + paymentId)
                .retrieve()
                .body(Boolean.class);
    }

    public PaymentInvoiceDto getInvoiceDataById(Long paymentId) {
        return restClient
                .get()
                .uri("/api/v1/payments/invoices/" + paymentId)
                .retrieve()
                .body(PaymentInvoiceDto.class);
    }
}