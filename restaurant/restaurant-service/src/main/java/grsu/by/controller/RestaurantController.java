package grsu.by.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import grsu.by.dto.restaurantDto.RestaurantApplicationDto;
import grsu.by.dto.restaurantDto.RestaurantCreationDto;
import grsu.by.dto.restaurantDto.RestaurantFullDto;
import grsu.by.dto.restaurantDto.RestaurantShortDto;
import grsu.by.enums.RestaurantStatus;
import grsu.by.service.RestaurantAdminService;
import grsu.by.service.RestaurantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/restaurants")
@Slf4j
public class RestaurantController {
    private final RestaurantService restaurantService;
    private final RestaurantAdminService restaurantAdminService;
    private final ObjectMapper objectMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('PLATFORM_ADMIN')")
    public RestaurantShortDto create(
            @RequestParam("restaurant") String jsonPart,
            @RequestPart(value = "photo", required = false) MultipartFile photo)
            throws JsonProcessingException
    {
        RestaurantCreationDto creationDto = objectMapper.readValue(jsonPart, RestaurantCreationDto.class);
        return restaurantService.create(creationDto, photo);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('PLATFORM_ADMIN') or " +
            "(hasRole('RESTAURANT_ADMIN') and @restaurantSecurity.isAdminOf(#id))")
    public RestaurantShortDto update(@PathVariable Long id,
                                     @RequestBody @Valid RestaurantCreationDto updateDto) {
        log.info("Update Restaurant {}", id);
        return restaurantService.update(id, updateDto);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('PLATFORM_ADMIN')")
    public Page<RestaurantShortDto> findAllAdmin(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {
        return restaurantService.findAll(
                PageRequest.of(page, size, Sort.by("id").descending())
        );
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public RestaurantFullDto findById(@PathVariable Long id) {
        log.info("Find Restaurant by id {}", id);
        return restaurantService.findById(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<RestaurantShortDto> findAll(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        return restaurantService.findActive(
                PageRequest.of(page, size, Sort.by("id").descending())
        );
    }

    @PutMapping(value = "/{id}/photo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('PLATFORM_ADMIN') or " +
            "(hasRole('RESTAURANT_ADMIN') and @restaurantSecurity.isAdminOf(#id))")
    public String uploadPhoto(@PathVariable Long id,
                              @RequestParam("photo") MultipartFile photo) {
        log.info("Upload photo for restaurant {}", id);
        return restaurantService.uploadPhoto(id, photo);
    }

    @DeleteMapping("/{id}/photo")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('PLATFORM_ADMIN') or " +
            "(hasRole('RESTAURANT_ADMIN') and @restaurantSecurity.isAdminOf(#id))")
    public void deletePhoto(@PathVariable Long id) {
        log.info("Delete photo for restaurant {}", id);
        restaurantService.deletePhoto(id);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public Page<RestaurantShortDto> search(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) BigDecimal minRating,
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size
    ) {
        log.info("Search Restaurants by name={} minRating={}", name, minRating);
        return restaurantService.search(name, minRating,
                PageRequest.of(page, size, Sort.by("id").descending())
        );
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('RESTAURANT_ADMIN')")
    public List<RestaurantShortDto> getMyRestaurants(
            @RequestHeader("X-Auth-Profile-Id") Long profileId) {
        return restaurantAdminService.getRestaurantsByProfileId(profileId);
    }

    @PostMapping(value = "/apply", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void applyForRegistration(
            @RequestParam("application") String jsonPart,
            @RequestPart(value = "photo", required = false) MultipartFile photo
    ) throws JsonProcessingException {
        RestaurantApplicationDto dto = objectMapper.readValue(jsonPart, RestaurantApplicationDto.class);
        restaurantService.applyForRegistration(dto, photo);
    }

    @PatchMapping("/{id}/approve")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('PLATFORM_ADMIN')")
    public RestaurantFullDto approveApplication(@PathVariable Long id) {
        log.info("Approve restaurant application {}", id);
        return restaurantService.approveApplication(id);
    }

    @PatchMapping("/{id}/reject")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('PLATFORM_ADMIN')")
    public RestaurantFullDto rejectApplication(@PathVariable Long id) {
        log.info("Reject restaurant application {}", id);
        return restaurantService.rejectApplication(id);
    }

    @GetMapping("/applications")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('PLATFORM_ADMIN')")
    public Page<RestaurantFullDto> findAllApplications(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return restaurantService.findByStatuses(
                List.of(RestaurantStatus.PENDING_ACTIVATION,
                        RestaurantStatus.ACTIVE,
                        RestaurantStatus.REJECTED),
                PageRequest.of(page, size, Sort.by("id").descending())
        );
    }

    @GetMapping("/pending")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('PLATFORM_ADMIN')")
    public Page<RestaurantFullDto> findPending(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return restaurantService.findByStatus(RestaurantStatus.PENDING_ACTIVATION,
                PageRequest.of(page, size));
    }
}
