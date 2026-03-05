package grsu.by;

import grsu.by.config.properties.InvoiceRestClientProperties;
import grsu.by.dto.InvoiceDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

public class InvoiceRestClient {
    private final RestClient restClient;

    public InvoiceRestClient(InvoiceRestClientProperties properties, RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder
                .baseUrl(properties.getBaseUrl())
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public byte[] createInvoiceByPaymentId(InvoiceDto invoiceDto) {
        return restClient
                .post()
                .uri("/api/v1/invoices")
                .body(invoiceDto)
                .retrieve()
                .body(byte[].class);
    }
}
