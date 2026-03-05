package grsu.by.service.impl;

import grsu.by.RestaurantRestClient;
import grsu.by.dto.PagedDataDto;
import grsu.by.dto.restaurantDto.RestaurantCreationDto;
import grsu.by.dto.restaurantDto.RestaurantShortDto;
import grsu.by.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRestClient restaurantRestClient;

    @Override
    public RestaurantShortDto create(RestaurantCreationDto creationDto) {
        return restaurantRestClient.createRestaurant(creationDto);
    }

    @Override
    public RestaurantShortDto findById(Long id) {
        return restaurantRestClient.findRestaurantById(id);
    }

    @Override
    public PagedDataDto<RestaurantShortDto> findAll(Integer page, Integer size) {
        return restaurantRestClient.findAllRestaurants(page, size);
    }
}
