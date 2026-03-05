package grsu.by.service;

import grsu.by.dto.PagedDataDto;
import grsu.by.dto.restaurantDto.RestaurantCreationDto;
import grsu.by.dto.restaurantDto.RestaurantShortDto;
import org.springframework.data.domain.Pageable;

public interface RestaurantService {
    RestaurantShortDto create(RestaurantCreationDto creationDto);
    RestaurantShortDto findById(Long id);
    PagedDataDto<RestaurantShortDto> findAll(Pageable pageable);
}
