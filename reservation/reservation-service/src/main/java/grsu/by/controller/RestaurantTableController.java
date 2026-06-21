package grsu.by.controller;

import grsu.by.dto.restaurantTableDto.RestaurantTableCreationDto;
import grsu.by.dto.restaurantTableDto.RestaurantTableFullDto;
import grsu.by.service.RestaurantTableService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/restaurant-tables")
@Slf4j
public class RestaurantTableController {

    private final RestaurantTableService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('PLATFORM_ADMIN') or hasRole('RESTAURANT_ADMIN')")
    public RestaurantTableFullDto create(@RequestBody @Valid RestaurantTableCreationDto creationDto) {
        log.info("Create RestaurantTable request received");
        return service.create(creationDto);
    }

    @PostMapping("/batch")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('PLATFORM_ADMIN') or hasRole('RESTAURANT_ADMIN')")
    public List<RestaurantTableFullDto> createAll(
            @RequestBody @Valid List<RestaurantTableCreationDto> tableCreationDtos) {
        log.info("Batch create {} RestaurantTables", tableCreationDtos.size());
        return service.createAll(tableCreationDtos);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public RestaurantTableFullDto findById(@PathVariable Long id) {
        log.info("Find RestaurantTable by id {}", id);
        return service.findById(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<RestaurantTableFullDto> findByRestaurantId(
            @RequestParam Long restaurantId) {
        return service.findByRestaurantId(restaurantId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('PLATFORM_ADMIN') or hasRole('RESTAURANT_ADMIN')")
    public void delete(@PathVariable Long id) {
        log.info("Delete RestaurantTable id={}", id);
        service.delete(id);
    }

    @DeleteMapping("/batch")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('PLATFORM_ADMIN') or hasRole('RESTAURANT_ADMIN')")
    public void deleteAll(@RequestBody List<Long> ids) {
        log.info("Batch delete {} RestaurantTables", ids.size());
        service.deleteAllByIds(ids);
    }
}