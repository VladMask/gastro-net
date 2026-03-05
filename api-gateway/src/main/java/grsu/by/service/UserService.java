package grsu.by.service;

import grsu.by.dto.UserShortDto;

public interface UserService {
    UserShortDto findById(Long id);
}
