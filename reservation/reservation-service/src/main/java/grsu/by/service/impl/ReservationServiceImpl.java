package grsu.by.service.impl;

import grsu.by.dto.reservationDto.ReservationCreationDto;
import grsu.by.dto.reservationDto.ReservationFullDto;
import grsu.by.entity.Reservation;
import grsu.by.entity.RestaurantTable;
import grsu.by.enums.ReservationStatus;
import grsu.by.enums.RestaurantTableStatus;
import grsu.by.repository.ReservationRepository;
import grsu.by.repository.RestaurantTableRepository;
import grsu.by.service.ReservationService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {
    private final ReservationRepository reservationRepository;
    private final RestaurantTableRepository restaurantTableRepository;
    private final ModelMapper mapper;

    @Transactional
    @Override
    public ReservationFullDto create(ReservationCreationDto creationDto) {
        List<RestaurantTable> tables = restaurantTableRepository.findAllById(creationDto.getRestaurantTablesIds());
        validateReservationCreation(creationDto, tables);
        Reservation reservation = mapper.map(creationDto, Reservation.class);
        tables.forEach(table -> table.setStatus(RestaurantTableStatus.RESERVED));
        if(reservation.getReservedUntil() == null) {
            reservation.setReservedUntil(creationDto.getReservedAt().plus(2, ChronoUnit.HOURS));
        }
        reservation.setRestaurantTables(tables);
        reservation.setStatus(ReservationStatus.CREATED);
        return mapper.map(reservationRepository.save(reservation), ReservationFullDto.class);
    }

    @Override
    public ReservationFullDto findById(Long id) {
        Reservation reservation = reservationRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Reservation not found")
        );
        return mapper.map(reservation, ReservationFullDto.class);
    }

    @Override
    public void confirmReservationById(Long id) {
        Reservation reservation = reservationRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Reservation not found")
        );
        reservation.setStatus(ReservationStatus.CONFIRMED);
        mapper.map(reservationRepository.save(reservation), ReservationFullDto.class);
    }

    private void validateReservationCreation(ReservationCreationDto creationDto, List<RestaurantTable> tables) {
        if(tables.isEmpty()) {
            throw new EntityNotFoundException("RestaurantTables not found");
        }
        if(creationDto.getGuestsCount() > tables.stream().mapToInt(RestaurantTable::getCapacity).sum()) {
            throw new IllegalArgumentException("Guests count cannot be greater than tables capacity");
        }
        if(tables.stream().anyMatch(t -> !t.getStatus().equals(RestaurantTableStatus.AVAILABLE))) {
            throw new IllegalArgumentException("Cannot create reservation with unavailable tables");
        }
        /*
        todo
        Изменить проверку на занятость стола
        Стол занят если уже есть бронь на ту же дату, что выбирает гость
         */
    }
}
