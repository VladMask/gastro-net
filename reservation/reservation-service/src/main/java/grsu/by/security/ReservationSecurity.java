package grsu.by.security;

import grsu.by.RestaurantRestClient;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component("reservationSecurity")
@RequiredArgsConstructor
public class ReservationSecurity {

    private final RestaurantRestClient restaurantRestClient;

    public boolean isAdminOfRestaurant(Long restaurantId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getPrincipal() == null) {
            return false;
        }
        ProfilePrincipal principal = (ProfilePrincipal) auth.getPrincipal();
        return restaurantRestClient.isAdminOfRestaurant(principal.getProfileId(), restaurantId);
    }
}