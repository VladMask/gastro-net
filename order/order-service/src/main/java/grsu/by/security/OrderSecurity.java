package grsu.by.security;

import grsu.by.RestaurantRestClient;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component("orderSecurity")
@RequiredArgsConstructor
public class OrderSecurity {

    private final RestaurantRestClient restaurantRestClient;

    public boolean isAdminOfRestaurant(Long restaurantId) {
        Long profileId = getCurrentProfileId();
        if (profileId == null || restaurantId == null) return false;
        return restaurantRestClient.isAdminOfRestaurant(profileId, restaurantId);
    }

    public Long getCurrentProfileId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof ProfilePrincipal principal)) {
            return null;
        }
        return principal.getProfileId();
    }
}