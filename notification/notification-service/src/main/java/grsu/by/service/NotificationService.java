package grsu.by.service;

import grsu.by.dto.NotificationDto;
import grsu.by.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NotificationService {
    Notification create(Long userId, String title, String body, String type);
    Page<NotificationDto> findByUserId(Long userId, Pageable pageable);
    void markAsRead(Long notificationId, Long currentUserId);
    void markAllAsRead(Long userId);
}
