package grsu.by.service;

import grsu.by.dto.mealDto.MealCreationDto;
import grsu.by.dto.mealDto.MealFullDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface MealService {
    MealFullDto create(MealCreationDto creationDto, MultipartFile photo);
    MealFullDto findById(Long id);
    Page<MealFullDto> findByRestaurantId(Long restaurantId, Pageable pageable);
    MealFullDto update(Long id, MealCreationDto updateDto);
    void delete(Long id);
    String uploadPhoto(Long mealId, MultipartFile file);
    void deletePhoto(Long mealId);
}
