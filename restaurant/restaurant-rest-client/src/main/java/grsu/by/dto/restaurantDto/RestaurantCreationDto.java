package grsu.by.dto.restaurantDto;

import grsu.by.dto.addressDto.AddressDto;
import jakarta.validation.constraints.NotBlank;
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
public class RestaurantCreationDto {
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    private String previewPhotoUrl;
    @NotBlank
    private String contactPhone;
    private String workingHours;
    @NotNull
    private AddressDto address;
}
