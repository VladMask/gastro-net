package grsu.by.dto.mealDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MealFullDto {
    private Long id;
    private String name;
    private String photoUrl;
    private String description;
    private String ingredients;
    private BigDecimal price;
    private Short proteins;
    private Short fats;
    private Short carbs;
    private Short calories;
    private Long categoryId;
    private Long restaurantId;
}
