package grsu.by.dto.mealDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
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
public class MealCreationDto {
    @NotBlank
    private String name;
    private String photoUrl;
    private String description;
    private String ingredients;
    @NotNull
    @Positive
    private BigDecimal price;
    @NotNull
    @PositiveOrZero
    private Short proteins;
    @NotNull
    @PositiveOrZero
    private Short fats;
    @NotNull
    @PositiveOrZero
    private Short carbs;
    @NotNull
    @PositiveOrZero
    private Short calories;
    @NotNull
    private Long categoryId;
    @NotNull
    private Long restaurantId;
}
