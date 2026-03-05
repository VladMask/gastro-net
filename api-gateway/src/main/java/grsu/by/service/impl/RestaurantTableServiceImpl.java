package grsu.by.service.impl;

import grsu.by.ReservationRestClient;
import grsu.by.dto.restaurantTableDto.RestaurantTableCreationDto;
import grsu.by.dto.restaurantTableDto.RestaurantTableFullDto;
import grsu.by.service.RestaurantTableService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RestaurantTableServiceImpl implements RestaurantTableService {
    private final ReservationRestClient reservationRestClient;

    @Override
    public RestaurantTableFullDto create(RestaurantTableCreationDto creationDto) {
        return reservationRestClient.createRestaurantTable(creationDto);
    }

    @Override
    public RestaurantTableFullDto findById(Long id) {
        return reservationRestClient.findRestaurantTableById(id);
    }
}
