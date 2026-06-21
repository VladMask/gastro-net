package grsu.by.service;

import grsu.by.dto.mealCategoryDto.MealCategoryDto;

import java.util.List;

public interface MealCategoryService {
    List<MealCategoryDto> findAll();
}
