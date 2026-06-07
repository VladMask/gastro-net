package grsu.by.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import grsu.by.dto.mealDto.MealCreationDto;
import grsu.by.dto.mealDto.MealFullDto;
import grsu.by.service.MealService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/meals")
@RequiredArgsConstructor
public class MealController {

    private final MealService service;
    private final ObjectMapper objectMapper;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('PLATFORM_ADMIN') or (hasRole('RESTAURANT_ADMIN') and @restaurantSecurity.isAdminOf(#restaurantId))")
    public MealFullDto create(
            @RequestParam("meal") String jsonPart,
            @RequestParam("restaurantId") Long restaurantId,
            @RequestPart(value = "photo", required = false) MultipartFile photo
    ) throws JsonProcessingException {
        MealCreationDto creationDto = objectMapper.readValue(jsonPart, MealCreationDto.class);
        return service.create(creationDto, photo);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public MealFullDto findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<MealFullDto> findByRestaurantId(
            @RequestParam Long restaurantId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return service.findByRestaurantId(restaurantId, PageRequest.of(page, size));
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('PLATFORM_ADMIN') or hasRole('RESTAURANT_ADMIN')")
    public MealFullDto update(@PathVariable Long id, @RequestBody @Valid MealCreationDto updateDto) {
        return service.update(id, updateDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('PLATFORM_ADMIN') or hasRole('RESTAURANT_ADMIN')")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @PutMapping(value = "/{id}/photo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('PLATFORM_ADMIN') or hasRole('RESTAURANT_ADMIN')")
    public String uploadPhoto(@PathVariable Long id,
                              @RequestParam("file") MultipartFile file) {
        return service.uploadPhoto(id, file);
    }

    @DeleteMapping("/{id}/photo")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('PLATFORM_ADMIN') or hasRole('RESTAURANT_ADMIN')")
    public void deletePhoto(@PathVariable Long id) {
        service.deletePhoto(id);
    }
}