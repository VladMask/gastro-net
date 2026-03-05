package grsu.by.controller;

import grsu.by.dto.orderDto.OrderCreationDto;
import grsu.by.dto.orderDto.OrderFullDto;
import grsu.by.dto.orderDto.OrderShortDto;
import grsu.by.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@RequestMapping("/api/v1/orders")
@Slf4j
public class OrderController {
    private final OrderService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderShortDto create(@RequestBody @Valid OrderCreationDto creationDto) {
        log.info("Create request received");
        return service.create(creationDto);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public OrderShortDto findById(@PathVariable Long id) {
        log.info("Find Order by id {}", id);
        return service.findById(id);
    }

    @GetMapping("/{id}/details")
    @ResponseStatus(HttpStatus.OK)
    public OrderFullDto findByIdWithDetails(@PathVariable Long id) {
        log.info("Find Order with details by id {}", id);
        return service.findByIdWithDetails(id);
    }
}
