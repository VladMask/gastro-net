package grsu.by.service;

import grsu.by.dto.restaurantTableDto.RestaurantTableCreationDto;
import grsu.by.dto.restaurantTableDto.RestaurantTableFullDto;
import grsu.by.enums.RestaurantTableStatus;

import java.util.List;

public interface RestaurantTableService {

    RestaurantTableFullDto create(RestaurantTableCreationDto creationDto);

    List<RestaurantTableFullDto> createAll(List<RestaurantTableCreationDto> creationDtos);

    RestaurantTableFullDto findById(Long id);

    List<RestaurantTableFullDto> findByRestaurantId(Long restaurantId);

    List<RestaurantTableFullDto> findByRestaurantIdAndStatus(Long restaurantId, RestaurantTableStatus status);

    RestaurantTableFullDto updateStatus(Long id, RestaurantTableStatus status);

    void delete(Long id);

    void deleteAllByIds(List<Long> ids);
}