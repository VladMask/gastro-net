package grsu.by.controller;

import grsu.by.dto.mealCategoryDto.MealCategoryDto;
import grsu.by.service.MealCategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/meal-categories")
@RequiredArgsConstructor
@Slf4j
public class MealCategoryController {

    private final MealCategoryService service;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<MealCategoryDto> findAll() {
        log.info("Get all meal categories");
        return service.findAll();
    }
}