package grsu.by.controller;

import grsu.by.service.RestaurantAdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/restaurants/{restaurantId}/admins")
@Slf4j
public class RestaurantAdminController {

    private final RestaurantAdminService service;

    @PostMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('PLATFORM_ADMIN')")
    public void assignAdmin(@PathVariable Long restaurantId, @RequestParam Long profileId) {
        log.info("Assign profile {} as admin of restaurant {}", profileId, restaurantId);
        service.assignAdmin(restaurantId, profileId);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('PLATFORM_ADMIN')")
    public void removeAdmin(@PathVariable Long restaurantId, @RequestParam Long profileId) {
        log.info("Remove profile {} from admins of restaurant {}", profileId, restaurantId);
        service.removeAdmin(restaurantId, profileId);
    }

    @GetMapping("/{profileId}")
    @ResponseStatus(HttpStatus.OK)
    public boolean isAdminOfRestaurant(@PathVariable Long restaurantId,
                                       @PathVariable Long profileId) {
        log.info("Check if profile {} is admin of restaurant {}", profileId, restaurantId);
        return service.isAdminOfRestaurant(restaurantId, profileId);
    }
}
