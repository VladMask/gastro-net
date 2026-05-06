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
    private String location;
    private String status;

    // Поля схемы зала
    private Double posX;
    private Double posY;
    private Double width;
    private Double height;
    private String shape;
    private String label;
}
