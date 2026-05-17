package grsu.by.service;

import grsu.by.dto.floorSchemaDto.FloorSchemaDto;

public interface FloorSchemaService {
    FloorSchemaDto findByRestaurantId(Long restaurantId);
    FloorSchemaDto save(FloorSchemaDto dto);
}
