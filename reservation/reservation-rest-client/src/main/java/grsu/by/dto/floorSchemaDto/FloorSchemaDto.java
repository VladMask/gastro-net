package grsu.by.dto.floorSchemaDto;

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
public class FloorSchemaDto {
    private Long restaurantId;
    private List<FloorElementDto> elements;
    private Double canvasWidth;
    private Double canvasHeight;
}
