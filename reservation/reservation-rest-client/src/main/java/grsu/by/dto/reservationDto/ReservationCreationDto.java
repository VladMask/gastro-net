package grsu.by.dto.reservationDto;


import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationCreationDto {
    @Positive
    private Long userId;
    @Positive
    private Long restaurantId;
    @FutureOrPresent
    private Instant reservedAt;
    @FutureOrPresent
    private Instant reservedUntil;
    @Positive
    private Short guestsCount;
    @NotEmpty
    private List<Long> restaurantTablesIds;
}

