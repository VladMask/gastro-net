package grsu.by.service;

import grsu.by.dto.EmailResponse;
import grsu.by.dto.UserCreationDto;
import grsu.by.dto.UserShortDto;

public interface UserService {
    Long create(UserCreationDto creationDto);
    UserShortDto findById(Long id);
    EmailResponse findUserEmail(Long userId);
}
