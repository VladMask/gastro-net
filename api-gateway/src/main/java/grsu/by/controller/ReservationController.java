package grsu.by.controller;

import grsu.by.dto.reservationDto.ReservationCreationDto;
import grsu.by.dto.reservationDto.ReservationFullDto;
import grsu.by.service.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api-gateway/v1/reservations")
public class ReservationController {
    private final ReservationService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReservationFullDto create(@RequestBody @Valid ReservationCreationDto creationDto) {
        return service.create(creationDto);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ReservationFullDto findById(@PathVariable Long id) {
        return service.findById(id);
    }
}
