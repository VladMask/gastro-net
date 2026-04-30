package grsu.by.repository;

import grsu.by.entity.RestaurantAdmin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantAdminRepository extends JpaRepository<RestaurantAdmin, Long> {
    boolean existsByProfileIdAndRestaurantId(Long profileId, Long restaurantId);
    void deleteByProfileIdAndRestaurantId(Long profileId, Long restaurantId);
}
