package grsu.by.service.impl;

import grsu.by.dto.reviewDto.ReviewCreationDto;
import grsu.by.dto.reviewDto.ReviewFullDto;
import grsu.by.entity.Review;
import grsu.by.repository.ReviewRepository;
import grsu.by.service.ReviewService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final ModelMapper mapper;

    @Transactional
    @Override
    public ReviewFullDto create(ReviewCreationDto creationDto) {
        Review review = mapper.map(creationDto, Review.class);
        return mapper.map(reviewRepository.save(review), ReviewFullDto.class);
    }

    @Override
    public List<ReviewFullDto> findByRestaurantId(Long restaurantId) {
        return reviewRepository.findByRestaurantId(restaurantId).stream()
                .map(r -> mapper.map(r, ReviewFullDto.class))
                .toList();
    }

    @Transactional
    @Override
    public void delete(Long id) {
        if (!reviewRepository.existsById(id)) {
            throw new EntityNotFoundException("Review not found");
        }
        reviewRepository.deleteById(id);
    }
}
