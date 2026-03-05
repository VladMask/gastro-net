package grsu.by.service.impl;

import grsu.by.dto.PagedDataDto;
import grsu.by.dto.restaurantDto.RestaurantCreationDto;
import grsu.by.dto.restaurantDto.RestaurantShortDto;
import grsu.by.entity.Restaurant;
import grsu.by.repository.RestaurantRepository;
import grsu.by.service.RestaurantService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final ModelMapper mapper;

    @Transactional
    @Override
    public RestaurantShortDto create(RestaurantCreationDto creationDto) {
        Restaurant restaurant = mapper.map(creationDto, Restaurant.class);
        restaurant.setRating(BigDecimal.ZERO);
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
}
