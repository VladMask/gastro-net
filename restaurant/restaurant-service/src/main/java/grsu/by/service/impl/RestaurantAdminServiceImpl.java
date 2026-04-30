package grsu.by.service.impl;

import grsu.by.AuthenticationRestClient;
import grsu.by.entity.RestaurantAdmin;
import grsu.by.repository.RestaurantAdminRepository;
import grsu.by.service.RestaurantAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RestaurantAdminServiceImpl implements RestaurantAdminService {

    private final RestaurantAdminRepository restaurantAdminRepository;
    private final AuthenticationRestClient authenticationRestClient;

    @Transactional
    @Override
    public void assignAdmin(Long restaurantId, Long profileId) {
        if (!restaurantAdminRepository.existsByProfileIdAndRestaurantId(profileId, restaurantId)) {
            restaurantAdminRepository.save(RestaurantAdmin.builder()
                    .profileId(profileId)
                    .restaurantId(restaurantId)
                    .build());
        }
        authenticationRestClient.assignRole(profileId, "RESTAURANT_ADMIN");
    }

    @Transactional
    @Override
    public void removeAdmin(Long restaurantId, Long profileId) {
        restaurantAdminRepository.deleteByProfileIdAndRestaurantId(profileId, restaurantId);
    }
}
