package grsu.by.controller;

import grsu.by.dto.NotificationDto;
import grsu.by.security.ProfilePrincipal;
import grsu.by.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/my")
    @ResponseStatus(HttpStatus.OK)
    public Page<NotificationDto> getMyNotifications(
            @AuthenticationPrincipal ProfilePrincipal principal,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return notificationService.findByUserId(principal.getProfileId(),
                PageRequest.of(page, size, Sort.by("createdAt").descending()));
    }

    @PatchMapping("/{id}/read")
    @ResponseStatus(HttpStatus.OK)
    public void markAsRead(@PathVariable Long id,
                           @AuthenticationPrincipal ProfilePrincipal principal) {
        notificationService.markAsRead(id, principal.getProfileId());
    }

    @PatchMapping("/read-all")
    @ResponseStatus(HttpStatus.OK)
    public void markAllAsRead(@AuthenticationPrincipal ProfilePrincipal principal) {
        notificationService.markAllAsRead(principal.getProfileId());
    }
}