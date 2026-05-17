package grsu.by.service.impl;

import grsu.by.dto.floorSchemaDto.FloorSchemaDto;
import grsu.by.entity.FloorSchema;
import grsu.by.repository.FloorSchemaRepository;
import grsu.by.service.FloorSchemaService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FloorSchemaServiceImpl implements FloorSchemaService {

    private final FloorSchemaRepository floorSchemaRepository;
    private final ModelMapper modelMapper;

    @Override
    public FloorSchemaDto findByRestaurantId(Long restaurantId) {
        FloorSchema schema = floorSchemaRepository.findByRestaurantId(restaurantId)
                .orElseGet(() -> FloorSchema.builder()
                        .restaurantId(restaurantId)
                        .canvasWidth(200.0)
                        .canvasHeight(140.0)
                        .build());
        return modelMapper.map(schema, FloorSchemaDto.class);
    }

    @Transactional
    @Override
    @SneakyThrows
    public FloorSchemaDto save(FloorSchemaDto dto) {
        FloorSchema schema = floorSchemaRepository.findByRestaurantId(dto.getRestaurantId())
                .orElseGet(() -> FloorSchema.builder()
                        .restaurantId(dto.getRestaurantId())
                        .build());

        schema.setElements(dto.getElements());
        schema.setCanvasWidth(dto.getCanvasWidth());
        schema.setCanvasHeight(dto.getCanvasHeight());

        return modelMapper.map(floorSchemaRepository.save(schema),  FloorSchemaDto.class);
    }

}