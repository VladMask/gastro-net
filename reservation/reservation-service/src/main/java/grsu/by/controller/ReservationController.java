package grsu.by.controller;

import grsu.by.dto.reservationDto.AvailableSlotDto;
import grsu.by.dto.reservationDto.ReservationCreationDto;
import grsu.by.dto.reservationDto.ReservationFullDto;
import grsu.by.enums.ReservationStatus;
import grsu.by.service.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reservations")
@Slf4j
public class ReservationController {

    private final ReservationService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReservationFullDto create(@RequestBody @Valid ReservationCreationDto creationDto) {
        log.info("Create reservation request received");
        return service.create(creationDto);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ReservationFullDto findById(@PathVariable Long id) {
        log.info("Find Reservation by id {}", id);
        return service.findById(id);
    }

    @PatchMapping("/{id}/confirm")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('PLATFORM_ADMIN') or hasRole('RESTAURANT_ADMIN')")
    public void confirmReservationById(@PathVariable Long id) {
        service.confirmReservationById(id);
    }

    @PatchMapping("/{id}/cancel")
    @ResponseStatus(HttpStatus.OK)
    public void cancelReservationById(@PathVariable Long id) {
        service.cancelReservationById(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<ReservationFullDto> findByUserId(
            @RequestParam Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        log.info("Find reservations for user {}", userId);
        Pageable pageable = PageRequest.of(page, size, Sort.by("reservedAt").descending());
        return service.findByUserId(userId, pageable);
    }

    @GetMapping("/restaurant/{restaurantId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('PLATFORM_ADMIN') or (hasRole('RESTAURANT_ADMIN') and @reservationSecurity.isAdminOfRestaurant(#restaurantId))")
    public Page<ReservationFullDto> findByRestaurantId(
            @PathVariable Long restaurantId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "reservedAt") String sortBy,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateTo
    ) {
        ReservationStatus statusEnum = status != null ? ReservationStatus.valueOf(status) : null;
        Instant from = dateFrom != null ? dateFrom.atStartOfDay(ZoneOffset.UTC).toInstant() : null;
        Instant to   = dateTo   != null ? dateTo.plusDays(1).atStartOfDay(ZoneOffset.UTC).toInstant() : null;
        String sortField = sortBy.equals("createdAt") ? "createdAt" : "reservedAt";
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortField).descending());
        return service.findByRestaurantIdWithFilters(restaurantId, statusEnum, from, to, pageable);
    }

    @GetMapping("/available-slots")
    @ResponseStatus(HttpStatus.OK)
    public List<AvailableSlotDto> getAvailableSlots(
            @RequestParam Long restaurantId,
            @RequestParam List<Long> tableIds,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return service.getAvailableSlots(restaurantId, tableIds, date);
    }
}