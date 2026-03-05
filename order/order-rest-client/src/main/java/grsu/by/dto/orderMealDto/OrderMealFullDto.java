package grsu.by.dto.orderMealDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderMealFullDto {
    private Long orderId;
    private Long mealId;
    private Short quantity;
    private BigDecimal price;
}
