package grsu.by.dto.reviewDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewFullDto {
    private Long id;
    private Long restaurantId;
    private Long userId;
    private Short rating;
    private String content;
    private Instant createdAt;
}
