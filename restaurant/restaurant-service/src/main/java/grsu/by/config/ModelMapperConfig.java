package grsu.by.config;

import grsu.by.dto.reviewDto.ReviewFullDto;
import grsu.by.entity.Review;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.modelmapper.config.Configuration.AccessLevel.PRIVATE;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(PRIVATE);

        mapper.typeMap(Review.class, ReviewFullDto.class)
                .addMapping(r -> r.getRestaurant().getId(), ReviewFullDto::setRestaurantId);

        return mapper;
    }
}
