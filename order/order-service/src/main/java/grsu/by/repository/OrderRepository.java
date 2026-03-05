package grsu.by.repository;

import grsu.by.entity.Order;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @EntityGraph(attributePaths = {"orderMeals"})
    Optional<Order> findWithDetailsById(Long id);
}
