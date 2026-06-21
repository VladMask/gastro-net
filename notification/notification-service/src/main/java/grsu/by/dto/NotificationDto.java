package grsu.by.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
public class NotificationDto {
    private Long id;
    private Long userId;
    private String title;
    private String body;
    private String type;
    private boolean read;
    private Instant createdAt;
}