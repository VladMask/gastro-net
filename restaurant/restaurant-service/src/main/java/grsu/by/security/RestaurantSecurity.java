package grsu.by.security;

import grsu.by.repository.RestaurantAdminRepository;
import grsu.by.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RestaurantSecurity {

    private final RestaurantAdminRepository restaurantAdminRepository;
    private final ReviewRepository reviewRepository;

    public boolean isAdminOf(Long restaurantId) {
        Long profileId = getCurrentProfileId();
        if (profileId == null || restaurantId == null) {
            return false;
        }
        return restaurantAdminRepository.existsByProfileIdAndRestaurantId(profileId, restaurantId);
    }

    public boolean isReviewAuthor() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof ProfilePrincipal principal)) {
            throw new RuntimeException();
        }
        return reviewRepository.existsByUserEmail(principal.getEmail());
    }

    private Long getCurrentProfileId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof ProfilePrincipal principal)) {
            return null;
        }
        return principal.getProfileId();
    }
}
