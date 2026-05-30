package grsu.by.dto.mealCategoryDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MealCategoryDto {
    private Long id;
    private String name;
    private Boolean isVegan;
}