package grsu.by.dto.orderDto;

import grsu.by.dto.orderMealDto.OrderMealFullDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderFullDto {
    private Long orderId;
    private BigDecimal totalPrice;
    private List<OrderMealFullDto> orderMeals;
}
