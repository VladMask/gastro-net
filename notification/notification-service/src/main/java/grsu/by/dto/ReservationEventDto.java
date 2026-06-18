package grsu.by.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReservationEventDto {
    private Long id;
    private Long userId;
    private Long restaurantId;
    private String reservedAt;
    private String reservedUntil;
    private Integer guestsCount;
    private String status;
}
