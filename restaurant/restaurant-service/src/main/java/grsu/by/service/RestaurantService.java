package grsu.by.service;

import grsu.by.dto.PagedDataDto;
import grsu.by.dto.restaurantDto.RestaurantCreationDto;
import grsu.by.dto.restaurantDto.RestaurantShortDto;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

import org.springframework.web.multipart.MultipartFile;

public interface RestaurantService {
    RestaurantShortDto create(RestaurantCreationDto creationDto, MultipartFile photo);
    RestaurantShortDto update(Long id, RestaurantCreationDto updateDto);
    RestaurantShortDto findById(Long id);
    PagedDataDto<RestaurantShortDto> findAll(Pageable pageable);
    PagedDataDto<RestaurantShortDto> search(String name, BigDecimal minRating, Pageable pageable);
    String uploadPhoto(Long restaurantId, MultipartFile file);
    void deletePhoto(Long restaurantId);
}
