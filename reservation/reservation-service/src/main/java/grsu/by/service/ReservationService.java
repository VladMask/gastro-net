package grsu.by.service;

import grsu.by.dto.reservationDto.ReservationCreationDto;
import grsu.by.dto.reservationDto.ReservationFullDto;

public interface ReservationService {
    ReservationFullDto create(ReservationCreationDto creationDto);
    ReservationFullDto findById(Long id);
    void confirmReservationById(Long id);
}
