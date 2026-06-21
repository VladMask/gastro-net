package grsu.by.repository;

import grsu.by.entity.RestaurantAdmin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RestaurantAdminRepository extends JpaRepository<RestaurantAdmin, Long> {
    boolean existsByProfileIdAndRestaurantId(Long profileId, Long restaurantId);
    void deleteByProfileIdAndRestaurantId(Long profileId, Long restaurantId);
    List<RestaurantAdmin> findByProfileId(Long profileId);
}
