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

import java.time.Instant;
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

        Instant reservedUntil = creationDto.getReservedUntil() != null
                ? creationDto.getReservedUntil()
                : creationDto.getReservedAt().plus(2, ChronoUnit.HOURS);

        boolean hasOverlap = reservationRepository.existsOverlappingReservation(
                creationDto.getRestaurantTablesIds(),
                creationDto.getReservedAt(),
                reservedUntil
        );
        if (hasOverlap) {
            throw new IllegalArgumentException("One or more tables are already reserved for the requested time slot");
        }

        Reservation reservation = mapper.map(creationDto, Reservation.class);
        reservation.setReservedUntil(reservedUntil);
        reservation.setRestaurantTables(tables);
        reservation.setStatus(ReservationStatus.CREATED);

        tables.forEach(table -> table.setStatus(RestaurantTableStatus.RESERVED));

        return mapper.map(reservationRepository.save(reservation), ReservationFullDto.class);
    }

    @Override
    public ReservationFullDto findById(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Reservation not found"));
        return mapper.map(reservation, ReservationFullDto.class);
    }

    @Transactional
    @Override
    public void confirmReservationById(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Reservation not found"));
        if (reservation.getStatus() != ReservationStatus.CREATED) {
            throw new IllegalStateException("Only CREATED reservations can be confirmed");
        }
        reservation.setStatus(ReservationStatus.CONFIRMED);
        reservationRepository.save(reservation);
    }

    @Transactional
    @Override
    public void cancelReservationById(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Reservation not found"));
        if (reservation.getStatus() == ReservationStatus.CANCELLED) {
            throw new IllegalStateException("Reservation is already cancelled");
        }
        reservation.setStatus(ReservationStatus.CANCELLED);
        reservation.getRestaurantTables()
                .forEach(table -> table.setStatus(RestaurantTableStatus.AVAILABLE));
        reservationRepository.save(reservation);
    }

    @Override
    public List<ReservationFullDto> findByUserId(Long userId) {
        return reservationRepository.findByUserId(userId).stream()
                .map(r -> mapper.map(r, ReservationFullDto.class))
                .toList();
    }

    @Override
    public List<ReservationFullDto> findByRestaurantId(Long restaurantId) {
        return reservationRepository.findByRestaurantId(restaurantId).stream()
                .map(r -> mapper.map(r, ReservationFullDto.class))
                .toList();
    }

    @Transactional
    @Override
    public void expireOutdatedReservations() {
        List<Reservation> toExpire = reservationRepository
                .findByStatusAndReservedUntilBefore(ReservationStatus.CREATED, Instant.now());
        toExpire.addAll(reservationRepository
                .findByStatusAndReservedUntilBefore(ReservationStatus.CONFIRMED, Instant.now()));

        toExpire.forEach(r -> {
            r.setStatus(ReservationStatus.EXPIRED);
            r.getRestaurantTables().forEach(t -> t.setStatus(RestaurantTableStatus.AVAILABLE));
        });
        reservationRepository.saveAll(toExpire);
    }

    private void validateReservationCreation(ReservationCreationDto creationDto, List<RestaurantTable> tables) {
        if (tables.isEmpty()) {
            throw new EntityNotFoundException("RestaurantTables not found");
        }
        if (creationDto.getGuestsCount() > tables.stream().mapToInt(RestaurantTable::getCapacity).sum()) {
            throw new IllegalArgumentException("Guests count cannot be greater than tables capacity");
        }
        boolean anyUnavailable = tables.stream()
                .anyMatch(t -> t.getStatus() == RestaurantTableStatus.UNAVAILABLE);
        if (anyUnavailable) {
            throw new IllegalArgumentException("One or more selected tables are unavailable");
        }
    }
}