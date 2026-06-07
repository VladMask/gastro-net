package grsu.by.service;

import grsu.by.dto.reviewDto.ReviewCreationDto;
import grsu.by.dto.reviewDto.ReviewFullDto;

import java.util.List;

public interface ReviewService {
    ReviewFullDto create(ReviewCreationDto creationDto);
    List<ReviewFullDto> findByRestaurantId(Long restaurantId);
    void delete(Long id);
}