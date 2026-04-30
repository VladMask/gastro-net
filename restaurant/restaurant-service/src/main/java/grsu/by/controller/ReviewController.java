package grsu.by.controller;

import grsu.by.dto.reviewDto.ReviewCreationDto;
import grsu.by.dto.reviewDto.ReviewFullDto;
import grsu.by.service.ReviewService;
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
@RequestMapping("/api/v1/reviews")
@Slf4j
public class ReviewController {
    private final ReviewService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("isAuthenticated()")
    public ReviewFullDto create(@RequestBody @Valid ReviewCreationDto creationDto) {
        log.info("Create review for restaurant {}", creationDto.getRestaurantId());
        return service.create(creationDto);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ReviewFullDto> findByRestaurantId(@RequestParam Long restaurantId) {
        log.info("Find reviews for restaurant {}", restaurantId);
        return service.findByRestaurantId(restaurantId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('PLATFORM_ADMIN')")
    public void delete(@PathVariable Long id) {
        log.info("Delete review {}", id);
        service.delete(id);
    }
}
