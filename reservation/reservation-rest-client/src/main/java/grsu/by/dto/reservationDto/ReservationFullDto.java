package grsu.by.dto.reservationDto;

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
public class ReservationFullDto {
    private Long userId;
    private Long restaurantId;
    private Instant reservedAt;
    private Short guestsCount;
    private Instant createdAt;
    private String status;
    private List<Long> restaurantTablesIds;
}
