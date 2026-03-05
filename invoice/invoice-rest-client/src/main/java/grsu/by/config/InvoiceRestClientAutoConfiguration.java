package grsu.by.config;

import grsu.by.InvoiceRestClient;
import grsu.by.config.properties.InvoiceRestClientProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
@ConditionalOnClass(InvoiceRestClient.class)
@EnableConfigurationProperties(InvoiceRestClientProperties.class)
public class InvoiceRestClientAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public InvoiceRestClient invoiceRestClient(InvoiceRestClientProperties properties, RestClient.Builder restClientBuilder) {
        return new InvoiceRestClient(properties, restClientBuilder);
    }
}
