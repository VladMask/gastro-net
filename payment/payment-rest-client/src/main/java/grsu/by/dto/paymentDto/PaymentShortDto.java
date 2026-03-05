package grsu.by.dto.paymentDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentShortDto {
    private BigDecimal amount;
    private String paymentMethod;
    private String status;
    private Instant createdAt;
}
