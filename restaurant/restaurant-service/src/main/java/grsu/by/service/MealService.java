package grsu.by.service;

import grsu.by.dto.mealDto.MealInvoiceInfo;

import java.util.List;

public interface MealService {
    List<MealInvoiceInfo> findByIds(List<Long> ids);
}
