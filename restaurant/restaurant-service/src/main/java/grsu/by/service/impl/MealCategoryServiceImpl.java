package grsu.by.service.impl;

import grsu.by.dto.mealCategoryDto.MealCategoryDto;
import grsu.by.repository.MealCategoryRepository;
import grsu.by.service.MealCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MealCategoryServiceImpl implements MealCategoryService {

    private final MealCategoryRepository repository;

    @Override
    public List<MealCategoryDto> findAll() {
        return repository.findAll().stream()
                .map(c -> MealCategoryDto.builder()
                        .id(c.getId())
                        .name(c.getName())
                        .isVegan(c.getIsVegan())
                        .build())
                .toList();
    }
}
