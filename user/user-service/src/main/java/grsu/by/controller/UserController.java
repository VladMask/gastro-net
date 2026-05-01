package grsu.by.controller;

import grsu.by.dto.EmailResponse;
import grsu.by.dto.UserCreationDto;
import grsu.by.dto.UserFullDto;
import grsu.by.dto.UserShortDto;
import grsu.by.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long create(@RequestBody UserCreationDto creationDto) {
        log.info("Create request received");
        return service.create(creationDto);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserShortDto findById(@PathVariable Long id) {
        log.info("Find User by id {}", id);
        return service.findById(id);
    }

    @GetMapping("/me")
    @ResponseStatus(HttpStatus.OK)
    public UserFullDto findMe(@RequestParam String email) {
        log.info("Find me by email {}", email);
        return service.findByEmail(email);
    }

    @GetMapping("/email/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public EmailResponse findUserEmail(@PathVariable Long userId) {
        log.info("Find User email by userId {}", userId);
        return service.findUserEmail(userId);
    }

}
