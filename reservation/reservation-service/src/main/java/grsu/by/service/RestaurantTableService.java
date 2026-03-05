package grsu.by.service;

import grsu.by.dto.restaurantTableDto.RestaurantTableCreationDto;
import grsu.by.dto.restaurantTableDto.RestaurantTableFullDto;

public interface RestaurantTableService {
    RestaurantTableFullDto create(RestaurantTableCreationDto creationDto);
    RestaurantTableFullDto findById(Long id);
}
