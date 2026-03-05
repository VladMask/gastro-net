package grsu.by.controller;

import grsu.by.service.ConfirmationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final ConfirmationService confirmationService;

    @PostMapping("/confirmation/{verificationCode}")
    @ResponseStatus(HttpStatus.OK)
    public boolean confirmVerificationCode(@PathVariable Integer verificationCode) {
        return confirmationService.confirmVerificationCode(verificationCode);
    }
}
