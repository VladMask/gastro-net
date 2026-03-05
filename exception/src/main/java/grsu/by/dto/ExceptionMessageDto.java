package grsu.by.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExceptionMessageDto implements Serializable {
    private HttpStatus status;
    private Integer code;
    private String serviceId;
    private String message;
    private String date;
}
