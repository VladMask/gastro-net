package grsu.by.controller;

import grsu.by.dto.mealDto.MealInvoiceInfo;
import grsu.by.service.MealService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/meals")
@RequiredArgsConstructor
public class MealsController {
    private final MealService service;

    @GetMapping("/info")
    @ResponseStatus(HttpStatus.OK)
    public List<MealInvoiceInfo> findByIds(@RequestParam List<Long> ids) {
        return service.findByIds(ids);
    }
}
