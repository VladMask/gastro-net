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
public class PaymentInvoiceDto {
    private Long orderId;
    private Long userId;
    private Long restaurantId;
    private BigDecimal amount;
    private Instant createdAt;
}
