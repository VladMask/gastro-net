package grsu.by.repository;

import grsu.by.entity.Reservation;
import grsu.by.enums.ReservationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long>,
        JpaSpecificationExecutor<Reservation> {

    @Query("""
        select count(r) > 0 from Reservation r
        join r.restaurantTables t
        where t.id in :tableIds
          and r.status not in ('CANCELLED', 'EXPIRED')
          and r.reservedAt < :reservedUntil
          and r.reservedUntil > :reservedAt
    """)
    boolean existsOverlappingReservation(
            @Param("tableIds") List<Long> tableIds,
            @Param("reservedAt") Instant reservedAt,
            @Param("reservedUntil") Instant reservedUntil
    );

    Page<Reservation> findByUserId(Long userId, Pageable pageable);

    Page<Reservation> findByRestaurantId(Long restaurantId, Pageable pageable);

    List<Reservation> findByStatusAndReservedUntilBefore(ReservationStatus status, Instant now);
}