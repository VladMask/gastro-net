package grsu.by.controller;

import grsu.by.dto.PagedDataDto;
import grsu.by.dto.restaurantDto.RestaurantCreationDto;
import grsu.by.dto.restaurantDto.RestaurantShortDto;
import grsu.by.service.RestaurantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/restaurants")
@Slf4j
public class RestaurantController {
    private final RestaurantService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RestaurantShortDto create(@RequestBody @Valid RestaurantCreationDto creationDto) {
        log.info("Create request received");
        return service.create(creationDto);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public RestaurantShortDto findById(@PathVariable Long id) {
        log.info("Find Restaurant by id {}", id);
        return service.findById(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PagedDataDto<RestaurantShortDto> findAll(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size
            ) {
        log.info("Find Restaurants for page {} and size {}", page, size);
        return service.findAll(PageRequest.of(page, size));
    }
}
