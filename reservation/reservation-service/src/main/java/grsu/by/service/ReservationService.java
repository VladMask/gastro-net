package grsu.by.service;

import grsu.by.dto.reservationDto.AvailableSlotDto;
import grsu.by.dto.reservationDto.ReservationCreationDto;
import grsu.by.dto.reservationDto.ReservationFullDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

public interface ReservationService {

    ReservationFullDto create(ReservationCreationDto creationDto);

    ReservationFullDto findById(Long id);

    void confirmReservationById(Long id);

    void cancelReservationById(Long id);

    Page<ReservationFullDto> findByUserId(Long userId, Pageable pageable);

    Page<ReservationFullDto> findByRestaurantId(Long restaurantId, Pageable pageable);

    void expireOutdatedReservations();

    List<AvailableSlotDto> getAvailableSlots(Long restaurantId, List<Long> tableIds, LocalDate date);

    Page<ReservationFullDto> findByRestaurantIdWithFilters(Long restaurantId, Instant dateFrom, Instant dateTo, Pageable pageable);
}