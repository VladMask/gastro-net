package grsu.by.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class NotificationEventDto {
    private Long userId;
    private Long orderId;
    private Long paymentId;
    private Long reservationId;
    private Long restaurantId;
}
