package grsu.by.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceDto {
    private String firstName;
    private String lastName;
    private String userEmail;
    private String restaurantName;
    private Long orderId;
    private Instant createdAt;
    private BigDecimal totalPrice;
    private List<OrderItem> items;
}
