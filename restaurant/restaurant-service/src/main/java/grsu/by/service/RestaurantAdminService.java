package grsu.by.service;

public interface RestaurantAdminService {
    void assignAdmin(Long restaurantId, Long profileId);
    void removeAdmin(Long restaurantId, Long profileId);
    boolean isAdminOfRestaurant(Long restaurantId, Long profileId);
}
