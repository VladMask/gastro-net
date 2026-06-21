package grsu.by.service.impl;

import grsu.by.AuthenticationRestClient;
import grsu.by.dto.restaurantDto.RestaurantShortDto;
import grsu.by.entity.RestaurantAdmin;
import grsu.by.repository.RestaurantAdminRepository;
import grsu.by.repository.RestaurantRepository;
import grsu.by.service.RestaurantAdminService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class RestaurantAdminServiceImpl implements RestaurantAdminService {

    private final RestaurantAdminRepository restaurantAdminRepository;
    private final RestaurantRepository restaurantRepository;
    private final ModelMapper mapper;
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

    @Override
    public boolean isAdminOfRestaurant(Long restaurantId, Long profileId) {
        return restaurantAdminRepository.existsByProfileIdAndRestaurantId(profileId, restaurantId);
    }

    public List<RestaurantShortDto> getRestaurantsByProfileId(Long profileId) {
        return restaurantAdminRepository.findByProfileId(profileId)
                .stream()
                .map(admin -> restaurantRepository.findById(admin.getRestaurantId()).orElse(null))
                .filter(Objects::nonNull)
                .map(r -> mapper.map(r, RestaurantShortDto.class))
                .toList();
    }
}
