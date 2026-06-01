package grsu.by.controller;

import grsu.by.dto.floorSchemaDto.FloorSchemaDto;
import grsu.by.service.FloorSchemaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/floor-schema")
@Slf4j
public class FloorSchemaController {

    private final FloorSchemaService floorSchemaService;

    @GetMapping("/{restaurantId}")
    @ResponseStatus(HttpStatus.OK)
    public FloorSchemaDto findByRestaurantId(@PathVariable Long restaurantId) {
        log.info("Get floor schema for restaurant {}", restaurantId);
        return floorSchemaService.findByRestaurantId(restaurantId);
    }

    @PutMapping("/{restaurantId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('PLATFORM_ADMIN') or " +
            "(hasRole('RESTAURANT_ADMIN') and @reservationSecurity.isAdminOfRestaurant(#restaurantId))")
    public FloorSchemaDto save(@PathVariable Long restaurantId,
                               @RequestBody FloorSchemaDto dto) {
        log.info("Save floor schema for restaurant {}", restaurantId);
        dto.setRestaurantId(restaurantId);
        return floorSchemaService.save(dto);
    }
}