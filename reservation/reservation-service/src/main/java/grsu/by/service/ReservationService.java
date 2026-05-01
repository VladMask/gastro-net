package grsu.by.service;

import grsu.by.dto.reservationDto.ReservationCreationDto;
import grsu.by.dto.reservationDto.ReservationFullDto;

import java.util.List;

public interface ReservationService {

    ReservationFullDto create(ReservationCreationDto creationDto);

    ReservationFullDto findById(Long id);

    void confirmReservationById(Long id);

    void cancelReservationById(Long id);

    List<ReservationFullDto> findByUserId(Long userId);

    List<ReservationFullDto> findByRestaurantId(Long restaurantId);

    void expireOutdatedReservations();
}