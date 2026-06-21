package grsu.by.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import grsu.by.dto.reservationDto.AvailableSlotDto;
import grsu.by.dto.reservationDto.ReservationCreationDto;
import grsu.by.dto.reservationDto.ReservationFullDto;
import grsu.by.entity.OutboxEvent;
import grsu.by.entity.Reservation;
import grsu.by.entity.RestaurantTable;
import grsu.by.enums.ReservationStatus;
import grsu.by.repository.ReservationRepository;
import grsu.by.repository.RestaurantTableRepository;
import grsu.by.security.ReservationSecurity;
import grsu.by.service.OutboxEventService;
import grsu.by.service.ReservationService;
import grsu.by.specification.ReservationSpecification;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final RestaurantTableRepository restaurantTableRepository;
    private final ModelMapper mapper;
    private final ReservationSecurity reservationSecurity;
    private final OutboxEventService outboxEventService;
    private final ObjectMapper objectMapper;

    @Transactional
    @Override
    public ReservationFullDto create(ReservationCreationDto creationDto) {
        List<RestaurantTable> tables = restaurantTableRepository
                .findAllById(creationDto.getRestaurantTablesIds());
        validateReservationCreation(creationDto, tables);
        Instant reservedUntil = creationDto.getReservedUntil() != null
                ? creationDto.getReservedUntil()
                : creationDto.getReservedAt().plus(2, ChronoUnit.HOURS);
        boolean hasOverlap = reservationRepository.existsOverlappingReservation(
                creationDto.getRestaurantTablesIds(),
                creationDto.getReservedAt(),
                reservedUntil);
        if (hasOverlap) {
            throw new IllegalArgumentException(
                    "One or more tables are already reserved for the requested time slot");
        }
        Reservation reservation = mapper.map(creationDto, Reservation.class);
        reservation.setReservedUntil(reservedUntil);
        reservation.setRestaurantTables(tables);
        reservation.setStatus(ReservationStatus.CREATED);
        Reservation saved = reservationRepository.save(reservation);

        produceEvent(saved, "reservation.created");

        return mapper.map(saved, ReservationFullDto.class);
    }

    @Override
    public ReservationFullDto findById(Long id) {
        return mapper.map(reservationRepository.findById(id)
                        .orElseThrow(() -> new EntityNotFoundException("Reservation not found")),
                ReservationFullDto.class);
    }

    @Transactional
    @Override
    public void confirmReservationById(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Reservation not found"));
        checkAdminOf(reservation.getRestaurantId());
        if (reservation.getStatus() != ReservationStatus.CREATED) {
            throw new IllegalStateException("Only CREATED reservations can be confirmed");
        }
        reservation.setStatus(ReservationStatus.CONFIRMED);
        Reservation saved = reservationRepository.save(reservation);
        produceEvent(saved, "reservation.confirmed");
    }

    @Transactional
    @Override
    public void cancelReservationById(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Reservation not found"));
        checkOwnerOrAdmin(reservation);
        if (reservation.getStatus() == ReservationStatus.CANCELLED) {
            throw new IllegalStateException("Reservation is already cancelled");
        }
        boolean isAdmin = reservationSecurity.isAdminOfRestaurant(reservation.getRestaurantId());
        if (!isAdmin) {
            Instant twoHoursBefore = reservation.getReservedAt().minus(2, ChronoUnit.HOURS);
            if (Instant.now().isAfter(twoHoursBefore)) {
                throw new IllegalStateException(
                        "Cannot cancel reservation less than 2 hours before start time");
            }
        }
        reservation.setStatus(ReservationStatus.CANCELLED);
        Reservation saved = reservationRepository.save(reservation);
        produceEvent(saved, "reservation.cancelled");
    }

    @Override
    public Page<ReservationFullDto> findByUserId(Long userId, Pageable pageable) {
        return reservationRepository.findByUserId(userId, pageable)
                .map(r -> mapper.map(r, ReservationFullDto.class));
    }

    @Override
    public Page<ReservationFullDto> findByRestaurantId(Long restaurantId, Pageable pageable) {
        return reservationRepository.findByRestaurantId(restaurantId, pageable)
                .map(r -> mapper.map(r, ReservationFullDto.class));
    }

    @Transactional
    @Override
    public void expireOutdatedReservations() {
        List<Reservation> toExpire = reservationRepository
                .findByStatusAndReservedUntilBefore(ReservationStatus.CREATED, Instant.now());
        toExpire.addAll(reservationRepository
                .findByStatusAndReservedUntilBefore(ReservationStatus.CONFIRMED, Instant.now()));
        toExpire.forEach(r -> r.setStatus(ReservationStatus.EXPIRED));
        reservationRepository.saveAll(toExpire);
    }

    @Override
    public List<AvailableSlotDto> getAvailableSlots(Long restaurantId, List<Long> tableIds, LocalDate date) {
        LocalTime openTime = LocalTime.of(8, 0);
        LocalTime closeTime = LocalTime.of(20, 0);
        int slotMinutes = 30;
        int durationHours = 2;

        List<AvailableSlotDto> result = new ArrayList<>();
        LocalTime current = openTime;

        while (!current.plusHours(durationHours).isAfter(closeTime)) {
            Instant slotStart = date.atTime(current).toInstant(ZoneOffset.UTC);
            Instant slotEnd = date.atTime(current.plusHours(durationHours)).toInstant(ZoneOffset.UTC);

            boolean hasOverlap = !tableIds.isEmpty() && reservationRepository.existsOverlappingReservation(tableIds, slotStart, slotEnd);

            if (!hasOverlap) {
                result.add(new AvailableSlotDto(slotStart, slotEnd));
            }
            current = current.plusMinutes(slotMinutes);
        }
        return result;
    }

    @Override
    public Page<ReservationFullDto> findByRestaurantIdWithFilters(
            Long restaurantId, ReservationStatus status,
            Instant dateFrom, Instant dateTo, Pageable pageable) {
        var spec = ReservationSpecification.filter(restaurantId, status, dateFrom, dateTo);
        return reservationRepository.findAll(spec, pageable)
                .map(r -> mapper.map(r, ReservationFullDto.class));
    }

    private void checkAdminOf(Long restaurantId) {
        if (!reservationSecurity.isAdminOfRestaurant(restaurantId)) {
            throw new AccessDeniedException("You are not an admin of this restaurant");
        }
    }

    private void checkOwnerOrAdmin(Reservation reservation) {
        Long currentProfileId = reservationSecurity.getCurrentProfileId();
        boolean isOwner = currentProfileId != null
                && currentProfileId.equals(reservation.getUserId());
        boolean isAdmin = reservationSecurity.isAdminOfRestaurant(reservation.getRestaurantId());
        if (!isOwner && !isAdmin) {
            throw new AccessDeniedException("Access denied");
        }
    }

    private void validateReservationCreation(ReservationCreationDto creationDto,
                                             List<RestaurantTable> tables) {
        if (tables.isEmpty()) {
            throw new EntityNotFoundException("RestaurantTables not found");
        }
        if (creationDto.getGuestsCount() > tables.stream()
                .mapToInt(RestaurantTable::getCapacity).sum()) {
            throw new IllegalArgumentException(
                    "Guests count cannot be greater than tables capacity");
        }
    }

    private void produceEvent(Reservation reservation, String header) {
        try {
            outboxEventService.create(new OutboxEvent(
                    header,
                    objectMapper.writeValueAsString(reservation))
            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}