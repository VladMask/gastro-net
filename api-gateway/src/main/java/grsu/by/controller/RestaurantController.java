package grsu.by.controller;

import grsu.by.dto.PagedDataDto;
import grsu.by.dto.restaurantDto.RestaurantCreationDto;
import grsu.by.dto.restaurantDto.RestaurantShortDto;
import grsu.by.service.RestaurantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api-gateway/v1/restaurants")
public class RestaurantController {
    private final RestaurantService service;

    @PostMapping
    public RestaurantShortDto create(@RequestBody @Valid RestaurantCreationDto creationDto) {
        return service.create(creationDto);
    }

    @GetMapping("/{id}")
    public RestaurantShortDto findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @GetMapping
    public PagedDataDto<RestaurantShortDto> findAll(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size
            ) {
        return service.findAll(page, size);
    }
}
