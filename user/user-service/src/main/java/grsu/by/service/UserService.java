package grsu.by.service;

import grsu.by.dto.EmailResponse;
import grsu.by.dto.UserCreationDto;
import grsu.by.dto.UserFullDto;
import grsu.by.dto.UserShortDto;
import grsu.by.dto.UserUpdateDto;

public interface UserService {
    Long create(UserCreationDto creationDto);
    UserShortDto findById(Long id);
    EmailResponse findUserEmail(Long userId);
    UserFullDto findByEmail(String email);
    UserFullDto updateMe(String email, UserUpdateDto dto);
}
