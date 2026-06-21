package grsu.by.service;

import grsu.by.dto.restaurantDto.RestaurantShortDto;

import java.util.List;

public interface RestaurantAdminService {
    void assignAdmin(Long restaurantId, Long profileId);
    void removeAdmin(Long restaurantId, Long profileId);
    boolean isAdminOfRestaurant(Long restaurantId, Long profileId);
    List<RestaurantShortDto> getRestaurantsByProfileId(Long profileId);
}
