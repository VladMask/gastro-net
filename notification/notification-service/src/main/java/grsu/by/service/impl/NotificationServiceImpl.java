package grsu.by.service.impl;

import grsu.by.dto.NotificationDto;
import grsu.by.entity.Notification;
import grsu.by.repository.NotificationRepository;
import grsu.by.service.NotificationService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository repository;
    private final ModelMapper mapper;

    @Transactional
    @Override
    public Notification create(Long userId, String title, String body, String type) {
        return repository.save(Notification.builder()
            .userId(userId)
            .title(title)
            .body(body)
            .type(type)
            .build());
    }

    @Override
    public Page<NotificationDto> findByUserId(Long userId, Pageable pageable) {
        return repository.findByUserId(userId, pageable)
            .map(n -> mapper.map(n, NotificationDto.class));
    }

    @Transactional
    @Override
    public void markAsRead(Long notificationId, Long currentUserId) {
        Notification notification = repository.findById(notificationId)
            .orElseThrow(() -> new EntityNotFoundException("Notification not found"));
        if (!notification.getUserId().equals(currentUserId)) {
            throw new AccessDeniedException("Access denied");
        }
        notification.setRead(true);
        repository.save(notification);
    }

    @Transactional
    @Override
    public void markAllAsRead(Long userId) {
        repository.markAllReadForUser(userId);
    }
}
