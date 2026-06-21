package grsu.by.repository;

import grsu.by.entity.FloorSchema;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FloorSchemaRepository extends JpaRepository<FloorSchema, Long> {
    Optional<FloorSchema> findByRestaurantId(Long restaurantId);
}
