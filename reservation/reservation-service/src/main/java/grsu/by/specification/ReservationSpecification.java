package grsu.by.specification;

import grsu.by.entity.Reservation;
import grsu.by.enums.ReservationStatus;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class ReservationSpecification {

    public static Specification<Reservation> filter(
            Long restaurantId,
            ReservationStatus status,
            Instant dateFrom,
            Instant dateTo
    ) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(cb.equal(root.get("restaurantId"), restaurantId));

            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }
            if (dateFrom != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("reservedAt"), dateFrom));
            }
            if (dateTo != null) {
                predicates.add(cb.lessThan(root.get("reservedAt"), dateTo));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}