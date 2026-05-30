package grsu.by.dto.paymentDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StripeSessionDto {
    private String clientSecret;
    private String stripePaymentIntentId;
    private Long paymentId;
}