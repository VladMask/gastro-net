package grsu.by.dto.restaurantTableDto;

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
public class RestaurantTableFullDto {
    private Long id;
    private Long restaurantId;
    private String number;
    private Short capacity;
}
