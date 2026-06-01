package grsu.by.service.impl;

import grsu.by.dto.PagedDataDto;
import grsu.by.dto.restaurantDto.RestaurantCreationDto;
import grsu.by.dto.restaurantDto.RestaurantShortDto;
import grsu.by.entity.Restaurant;
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
import java.util.List;
import java.util.stream.Collectors;

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
        mapper.map(updateDto, restaurant);
        return mapper.map(restaurantRepository.save(restaurant), RestaurantShortDto.class);
    }

    @Override
    public RestaurantShortDto findById(Long id) {
        Restaurant restaurant = restaurantRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Restaurant not found")
        );
        return mapper.map(restaurant, RestaurantShortDto.class);
    }

    @Override
    public PagedDataDto<RestaurantShortDto> findAll(Pageable pageable) {
        Page<Restaurant> restaurants = restaurantRepository.findAll(pageable);
        List<RestaurantShortDto> dtos = restaurants.stream()
                .map(r -> mapper.map(r, RestaurantShortDto.class))
                .collect(Collectors.toList());
        return new PagedDataDto<>(dtos, restaurants.getTotalElements());
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

    @Override
    public PagedDataDto<RestaurantShortDto> search(String name, BigDecimal minRating, Pageable pageable) {
        Specification<Restaurant> spec = RestaurantSpecification.nameLike(name)
                .and(RestaurantSpecification.minRating(minRating));

        Page<Restaurant> page = restaurantRepository.findAll(spec, pageable);

        List<RestaurantShortDto> data = page.getContent().stream()
                .map(r -> mapper.map(r, RestaurantShortDto.class))
                .toList();

        return new PagedDataDto<>(data, page.getTotalElements());
    }
}
