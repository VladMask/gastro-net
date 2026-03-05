package grsu.by.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.math.BigDecimal;


@Getter
@Setter
@ConfigurationProperties(prefix = "payment")
public class PaymentServiceProperties {
    private Service service;
    
    @Getter
    @Setter
    public static class Service{
        private BigDecimal prepaymentPercentage;
    }
}
