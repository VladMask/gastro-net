package grsu.by.dto.restaurantDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import grsu.by.dto.addressDto.AddressDto;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantShortDto {
    private Long id;
    private String name;
    private String description;
    private BigDecimal rating;
    private String previewPhotoUrl;
    private String contactPhone;
    private String workingHours;
    private AddressDto address;
}
