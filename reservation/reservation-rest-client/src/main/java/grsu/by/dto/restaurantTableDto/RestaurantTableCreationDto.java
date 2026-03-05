package grsu.by.dto.restaurantTableDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantTableCreationDto {
    @Positive
    private Long restaurantId;
    @NotBlank
    private String number;
    @Positive
    private Short capacity;
    @NotBlank
    private String location;
}
