package grsu.by.dto.restaurantDto;

import grsu.by.dto.addressDto.AddressDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantFullDto {
    private Long id;
    private String name;
    private String description;
    private BigDecimal rating;
    private String previewPhotoUrl;
    private String ownerEmail;
    private String contactPhone;
    private String workingHours;
    private String status;
    private AddressDto address;
}