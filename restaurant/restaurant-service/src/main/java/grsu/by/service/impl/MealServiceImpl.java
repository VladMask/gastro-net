package grsu.by.service.impl;

import grsu.by.dto.mealDto.MealInvoiceInfo;
import grsu.by.entity.Meal;
import grsu.by.repository.MealRepository;
import grsu.by.service.MealService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MealServiceImpl implements MealService {
    private final MealRepository repository;
    private final ModelMapper mapper;

    @Override
    public List<MealInvoiceInfo> findByIds(List<Long> ids) {
        List<Meal> meals = repository.findAllById(ids);
        return meals.stream()
                .map(meal -> mapper.map(meal, MealInvoiceInfo.class))
                .toList();
    }
}
