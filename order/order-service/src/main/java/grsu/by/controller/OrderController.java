package grsu.by.controller;

import grsu.by.dto.orderDto.OrderCreationDto;
import grsu.by.dto.orderDto.OrderFullDto;
import grsu.by.dto.orderDto.OrderShortDto;
import grsu.by.enums.OrderStatus;
import grsu.by.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
@Slf4j
public class OrderController {

    private final OrderService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderShortDto create(
            @RequestBody @Valid OrderCreationDto creationDto,
            @RequestHeader("X-Auth-Profile-Id") Long profileId) {
        log.info("Create order request received for profileId={}", profileId);
        creationDto.setUserId(profileId);
        return service.create(creationDto);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public OrderShortDto findById(@PathVariable Long id) {
        log.info("Find Order by id={}", id);
        return service.findById(id);
    }

    @GetMapping("/{id}/details")
    @ResponseStatus(HttpStatus.OK)
    public OrderFullDto findByIdWithDetails(@PathVariable Long id) {
        log.info("Find Order with details by id={}", id);
        return service.findByIdWithDetails(id);
    }

    @GetMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    public List<OrderShortDto> findByUserId(@RequestParam Long userId) {
        log.info("Find Orders for user={}", userId);
        return service.findByUserId(userId);
    }

    @GetMapping("/restaurants")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('PLATFORM_ADMIN') or (hasRole('RESTAURANT_ADMIN') and @orderSecurity.isAdminOfRestaurant(#restaurantId))")
    public List<OrderShortDto> findByRestaurantId(@RequestParam Long restaurantId) {
        log.info("Find Orders for restaurant={}", restaurantId);
        return service.findByRestaurantId(restaurantId);
    }

    @PatchMapping("/{id}/status")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('PLATFORM_ADMIN') or hasRole('RESTAURANT_ADMIN')")
    public OrderShortDto updateStatus(@PathVariable Long id,
                                      @RequestParam OrderStatus status) {
        log.info("Update Order id={} status to {}", id, status);
        return service.updateStatus(id, status);
    }
}