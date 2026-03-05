package grsu.by.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserShortDto {
    private String firstname;
    private String lastname;
    private String email;
    private LocalDate birthDate;
    private Instant createdAt;
    private Instant updatedAt;
}
