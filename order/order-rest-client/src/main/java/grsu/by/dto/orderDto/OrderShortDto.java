package grsu.by.dto.orderDto;

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
public class OrderShortDto {
    private Long id;
    private Long restaurantId;
    private Long userId;
    private Long reservationId;
    private Instant createdAt;
    private String status;
    private BigDecimal totalPrice;
}
