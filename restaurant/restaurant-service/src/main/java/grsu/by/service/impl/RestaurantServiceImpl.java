package grsu.by.service.impl;

import grsu.by.dto.restaurantDto.RestaurantApplicationDto;
import grsu.by.dto.restaurantDto.RestaurantCreationDto;
import grsu.by.dto.restaurantDto.RestaurantFullDto;
import grsu.by.dto.restaurantDto.RestaurantShortDto;
import grsu.by.entity.Restaurant;
import grsu.by.enums.RestaurantStatus;
import grsu.by.repository.RestaurantRepository;
import grsu.by.service.RestaurantService;
import grsu.by.service.StorageService;
import grsu.by.specification.RestaurantSpecification;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final ModelMapper mapper;
    private final StorageService storageService;

    @Transactional
    @Override
    public RestaurantShortDto create(RestaurantCreationDto creationDto, MultipartFile photo) {
        Restaurant restaurant = mapper.map(creationDto, Restaurant.class);
        restaurant.setRating(BigDecimal.ZERO);

        restaurant = restaurantRepository.save(restaurant);

        if (photo != null) {
            uploadPhoto(restaurant.getId(), photo);
        }

        restaurant = restaurantRepository.save(restaurant);

        return mapper.map(restaurant, RestaurantShortDto.class);
    }

    @Transactional
    @Override
    public RestaurantShortDto update(Long id, RestaurantCreationDto updateDto) {
        Restaurant restaurant = restaurantRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Restaurant not found")
        );

        String existingPhotoUrl = restaurant.getPreviewPhotoUrl();
        mapper.map(updateDto, restaurant);
        restaurant.setPreviewPhotoUrl(existingPhotoUrl);

        return mapper.map(restaurantRepository.save(restaurant), RestaurantShortDto.class);
    }

    @Override
    public RestaurantFullDto findById(Long id) {
        Restaurant restaurant = restaurantRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Restaurant not found")
        );
        return mapper.map(restaurant, RestaurantFullDto.class);
    }

    @Override
    public Page<RestaurantShortDto> findActive(Pageable pageable) {
        return restaurantRepository.findByStatus(RestaurantStatus.ACTIVE, pageable)
                .map(r -> mapper.map(r, RestaurantShortDto.class));
    }

    @Override
    public Page<RestaurantShortDto> findAll(Pageable pageable) {
        return restaurantRepository.findAll(pageable)
                .map(r -> mapper.map(r, RestaurantShortDto.class));
    }

    @Override
    public Page<RestaurantShortDto> search(String name, BigDecimal minRating, Pageable pageable) {
        Specification<Restaurant> spec = RestaurantSpecification.nameLike(name)
                .and(RestaurantSpecification.minRating(minRating))
                .and(RestaurantSpecification.hasStatus(RestaurantStatus.ACTIVE));
        return restaurantRepository.findAll(spec, pageable)
                .map(r -> mapper.map(r, RestaurantShortDto.class));
    }

    @Transactional
    @Override
    public String uploadPhoto(Long restaurantId, MultipartFile file) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(
                () -> new EntityNotFoundException("Restaurant not found")
        );

        if (restaurant.getPreviewPhotoUrl() != null) {
            storageService.delete(restaurant.getPreviewPhotoUrl());
        }
        String url = storageService.upload("restaurants/" + restaurantId, file);
        restaurant.setPreviewPhotoUrl(url);
        restaurantRepository.save(restaurant);
        return url;
    }

    @Transactional
    @Override
    public void deletePhoto(Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(
                () -> new EntityNotFoundException("Restaurant not found")
        );
        if (restaurant.getPreviewPhotoUrl() != null) {
            storageService.delete(restaurant.getPreviewPhotoUrl());
            restaurant.setPreviewPhotoUrl(null);
            restaurantRepository.save(restaurant);
        }
    }

    @Transactional
    public void applyForRegistration(RestaurantApplicationDto dto, MultipartFile photo) {
        Restaurant restaurant = mapper.map(dto, Restaurant.class);
        restaurant.setStatus(RestaurantStatus.PENDING_ACTIVATION);
        restaurant.setRating(BigDecimal.ZERO);

        restaurant = restaurantRepository.save(restaurant);

        if (photo != null) {
            uploadPhoto(restaurant.getId(), photo);
        }

        restaurantRepository.save(restaurant);
    }

    @Transactional
    public RestaurantFullDto approveApplication(Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new EntityNotFoundException("Restaurant not found"));
        if (restaurant.getStatus() != RestaurantStatus.PENDING_ACTIVATION) {
            throw new IllegalStateException("Restaurant is not pending activation");
        }
        restaurant.setStatus(RestaurantStatus.ACTIVE);
        return mapper.map(restaurantRepository.save(restaurant), RestaurantFullDto.class);
    }

    @Transactional
    public RestaurantFullDto rejectApplication(Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new EntityNotFoundException("Restaurant not found"));
        restaurant.setStatus(RestaurantStatus.INACTIVE);
        return mapper.map(restaurantRepository.save(restaurant), RestaurantFullDto.class);
    }

    public Page<RestaurantFullDto> findByStatus(RestaurantStatus status, Pageable pageable) {
        return restaurantRepository.findByStatus(status, pageable)
                .map(r -> mapper.map(r, RestaurantFullDto.class));
    }
}
