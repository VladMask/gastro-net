package grsu.by.service;

import grsu.by.dto.mealDto.MealCreationDto;
import grsu.by.dto.mealDto.MealFullDto;
import grsu.by.dto.mealDto.MealInvoiceInfo;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface MealService {
    List<MealInvoiceInfo> findByIds(List<Long> ids);
    MealFullDto create(MealCreationDto creationDto, MultipartFile photo);
    MealFullDto findById(Long id);
    List<MealFullDto> findByRestaurantId(Long restaurantId);
    MealFullDto update(Long id, MealCreationDto updateDto);
    void delete(Long id);
    String uploadPhoto(Long mealId, MultipartFile file);
    void deletePhoto(Long mealId);
}
