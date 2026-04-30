package grsu.by.dto.reviewDto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewCreationDto {
    @NotNull
    private Long restaurantId;
    @NotNull
    private Long userId;
    @NotNull
    @Min(1)
    @Max(5)
    private Short rating;
    private String content;
}
