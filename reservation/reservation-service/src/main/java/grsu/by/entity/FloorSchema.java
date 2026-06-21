package grsu.by.entity;

import grsu.by.converter.FloorElementsConverter;
import grsu.by.dto.floorSchemaDto.FloorElementDto;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
@Entity
@Table(name = "floor_schemas")
public class FloorSchema {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "restaurant_id", nullable = false, unique = true)
    private Long restaurantId;

    @Convert(converter = FloorElementsConverter.class)
    @Column(name = "elements", columnDefinition = "TEXT")
    private List<FloorElementDto> elements;

    @Column(name = "canvas_width")
    private Double canvasWidth;

    @Column(name = "canvas_height")
    private Double canvasHeight;
}
