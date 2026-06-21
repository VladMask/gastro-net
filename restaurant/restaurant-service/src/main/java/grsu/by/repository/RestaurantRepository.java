package grsu.by.repository;

import grsu.by.entity.Restaurant;
import grsu.by.enums.RestaurantStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long>,
        JpaSpecificationExecutor<Restaurant> {

    Page<Restaurant> findByStatus(RestaurantStatus status, Pageable pageable);

    Page<Restaurant> findByStatusIn(List<RestaurantStatus> statuses, Pageable pageable);
}
