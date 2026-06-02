package grsu.by.service;

import grsu.by.dto.restaurantDto.RestaurantApplicationDto;
import grsu.by.dto.restaurantDto.RestaurantCreationDto;
import grsu.by.dto.restaurantDto.RestaurantFullDto;
import grsu.by.dto.restaurantDto.RestaurantShortDto;
import grsu.by.enums.RestaurantStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

public interface RestaurantService {
    RestaurantShortDto create(RestaurantCreationDto creationDto, MultipartFile photo);
    RestaurantShortDto update(Long id, RestaurantCreationDto updateDto);
    RestaurantFullDto findById(Long id);
    Page<RestaurantShortDto> findActive(Pageable pageable);
    Page<RestaurantShortDto> findAll(Pageable pageable);
    Page<RestaurantShortDto> search(String name, BigDecimal minRating, Pageable pageable);
    String uploadPhoto(Long restaurantId, MultipartFile file);
    void deletePhoto(Long restaurantId);
    void applyForRegistration(RestaurantApplicationDto dto, MultipartFile photo);
    Page<RestaurantFullDto> findByStatus(RestaurantStatus status, Pageable pageable);
    RestaurantFullDto approveApplication(Long restaurantId);
    RestaurantFullDto rejectApplication(Long restaurantId);
}
