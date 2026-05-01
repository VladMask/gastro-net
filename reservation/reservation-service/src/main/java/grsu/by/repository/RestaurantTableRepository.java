package grsu.by.repository;

import grsu.by.entity.RestaurantTable;
import grsu.by.enums.RestaurantTableStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RestaurantTableRepository extends JpaRepository<RestaurantTable, Long> {

    List<RestaurantTable> findByRestaurantId(Long restaurantId);

    List<RestaurantTable> findByRestaurantIdAndStatus(Long restaurantId, RestaurantTableStatus status);
}