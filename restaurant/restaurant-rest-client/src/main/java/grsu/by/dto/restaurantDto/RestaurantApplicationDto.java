package grsu.by.dto.restaurantDto;

import grsu.by.dto.addressDto.AddressDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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
public class RestaurantApplicationDto {

    @NotBlank
    private String name;
    private String description;
    @NotBlank
    @Email
    private String ownerEmail;
    @NotBlank
    private String contactPhone;
    private String workingHours;
    @Valid
    private AddressDto address;
}