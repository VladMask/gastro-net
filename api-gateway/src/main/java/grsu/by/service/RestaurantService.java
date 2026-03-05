package grsu.by.service;

import grsu.by.dto.PagedDataDto;
import grsu.by.dto.restaurantDto.RestaurantCreationDto;
import grsu.by.dto.restaurantDto.RestaurantShortDto;

public interface RestaurantService {
    RestaurantShortDto create(RestaurantCreationDto creationDto);
    RestaurantShortDto findById(Long id);
    PagedDataDto<RestaurantShortDto> findAll(Integer page, Integer size);
}
