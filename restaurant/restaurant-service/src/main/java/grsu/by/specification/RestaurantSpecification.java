package grsu.by.specification;

import grsu.by.entity.Restaurant;
import grsu.by.enums.RestaurantStatus;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public class RestaurantSpecification {

    public static Specification<Restaurant> nameLike(String name) {
        return (root, query, cb) -> {
            if (name == null || name.isBlank()) return cb.conjunction();
            return cb.like(cb.lower(root.get("name")),
                    "%" + name.toLowerCase() + "%");
        };
    }

    public static Specification<Restaurant> minRating(BigDecimal minRating) {
        return (root, query, cb) -> {
            if (minRating == null) return cb.conjunction();
            return cb.greaterThanOrEqualTo(root.get("rating"), minRating);
        };
    }

    public static Specification<Restaurant> hasStatus(RestaurantStatus status) {
        return (root, query, cb) -> cb.equal(root.get("status"), status);
    }
}