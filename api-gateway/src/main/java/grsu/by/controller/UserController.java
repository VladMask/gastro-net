package grsu.by.controller;

import grsu.by.dto.UserShortDto;
import grsu.by.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api-gateway/v1/users")
@Slf4j
public class UserController {
    private final UserService service;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserShortDto findById(@PathVariable Long id) {
        log.info("Find User by id {}", id);
        return service.findById(id);
    }
}
