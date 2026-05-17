package grsu.by.dto.floorSchemaDto;

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
public class FloorElementDto {
    private String id;
    private String type;
    private Double x;
    private Double y;
    private Double width;
    private Double height;
    private Double rotation;
    private String presetId;
    private Long tableId;
}
