package grsu.by.service.impl;

import grsu.by.dto.mealDto.MealCreationDto;
import grsu.by.dto.mealDto.MealFullDto;
import grsu.by.entity.Meal;
import grsu.by.entity.MealCategory;
import grsu.by.entity.Restaurant;
import grsu.by.repository.MealCategoryRepository;
import grsu.by.repository.MealRepository;
import grsu.by.repository.RestaurantRepository;
import grsu.by.security.RestaurantSecurity;
import grsu.by.service.MealService;
import grsu.by.service.StorageService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MealServiceImpl implements MealService {
    private final MealRepository mealRepository;
    private final MealCategoryRepository mealCategoryRepository;
    private final RestaurantRepository restaurantRepository;
    private final ModelMapper mapper;
    private final RestaurantSecurity restaurantSecurity;
    private final StorageService storageService;

    @Transactional
    @Override
    public MealFullDto create(MealCreationDto creationDto, MultipartFile photo) {
        Meal meal = mapper.map(creationDto, Meal.class);
        MealCategory category = mealCategoryRepository.findById(creationDto.getCategoryId())
                .orElseThrow(
                        () -> new EntityNotFoundException("MealCategory not found")
                );
        meal.setCategory(category);
        Restaurant restaurant = restaurantRepository.findById(creationDto.getRestaurantId())
                .orElseThrow(
                        () -> new EntityNotFoundException("Restaurant not found")
                );
        meal.setRestaurant(restaurant);

        meal = mealRepository.save(meal);

        if (photo != null) {
            String url = storageService.upload("meals/" + meal.getId(), photo);
            meal.setPhotoUrl(url);
            mealRepository.save(meal);
        }

        return toDto(meal);
    }

    @Override
    public MealFullDto findById(Long id) {
        return toDto(mealRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Meal not found")
        ));
    }

    @Override
    public List<MealFullDto> findByRestaurantId(Long restaurantId) {
        return mealRepository.findByRestaurantId(restaurantId).stream()
                .map(this::toDto)
                .toList();
    }

    @Transactional
    @Override
    public MealFullDto update(Long id, MealCreationDto updateDto) {
        Meal meal = mealRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Meal not found")
        );
        mapper.map(updateDto, meal);
        meal.setCategory(new MealCategory());
        meal.getCategory().setId(updateDto.getCategoryId());
        meal.setRestaurant(new Restaurant());
        meal.getRestaurant().setId(updateDto.getRestaurantId());
        return toDto(mealRepository.save(meal));
    }

    @Transactional
    @Override
    public void delete(Long id) {
        Meal meal = mealRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Meal not found")
        );
        checkModifyAccess(meal);
        mealRepository.deleteById(id);
    }

    @Transactional
    @Override
    public String uploadPhoto(Long mealId, MultipartFile file) {
        Meal meal = mealRepository.findById(mealId).orElseThrow(
                () -> new EntityNotFoundException("Meal not found")
        );
        checkModifyAccess(meal);
        if (meal.getPhotoUrl() != null) {
            storageService.delete(meal.getPhotoUrl());
        }
        String url = storageService.upload("meals/" + mealId, file);
        meal.setPhotoUrl(url);
        mealRepository.save(meal);
        return url;
    }

    @Transactional
    @Override
    public void deletePhoto(Long mealId) {
        Meal meal = mealRepository.findById(mealId).orElseThrow(
                () -> new EntityNotFoundException("Meal not found")
        );
        checkModifyAccess(meal);
        if (meal.getPhotoUrl() != null) {
            storageService.delete(meal.getPhotoUrl());
            meal.setPhotoUrl(null);
            mealRepository.save(meal);
        }
    }

    private void checkModifyAccess(Meal meal) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isPlatformAdmin = auth != null && auth.getAuthorities()
                .contains(new SimpleGrantedAuthority("ROLE_PLATFORM_ADMIN"));
        if (!isPlatformAdmin) {
            Long restaurantId = meal.getRestaurant() != null ? meal.getRestaurant().getId() : null;
            if (!restaurantSecurity.isAdminOf(restaurantId)) {
                throw new AccessDeniedException("You are not an admin of this restaurant");
            }
        }
    }

    private MealFullDto toDto(Meal meal) {
        MealFullDto dto = mapper.map(meal, MealFullDto.class);
        if (meal.getCategory() != null) {
            dto.setCategoryId(meal.getCategory().getId());
        }
        if (meal.getRestaurant() != null) {
            dto.setRestaurantId(meal.getRestaurant().getId());
        }
        return dto;
    }
}
