package grsu.by.dto.orderDto;

import grsu.by.dto.orderMealDto.OrderMealCreationDto;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderCreationDto {
    @Positive
    private Long restaurantId;
    private Long userId;
    @NotEmpty
    private List<OrderMealCreationDto> orderMeals;
}
