package grsu.by.repository;

import grsu.by.entity.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    @Query("select r from Restaurant r where " +
           "(:name is null or lower(r.name) like lower(concat('%', :name, '%'))) AND " +
           "(:minRating is null or r.rating >= :minRating)")
    Page<Restaurant> findByNameAndRating(
            @Param("name") String name,
            @Param("minRating") BigDecimal minRating,
            Pageable pageable);
}
