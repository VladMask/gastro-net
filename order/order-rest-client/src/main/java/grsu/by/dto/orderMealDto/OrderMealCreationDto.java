package grsu.by.dto.orderMealDto;

import jakarta.validation.constraints.Positive;
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
public class OrderMealCreationDto {
    @Positive
    private Long mealId;
    @Positive
    private Short quantity;
    @Positive
    private BigDecimal price;
}
