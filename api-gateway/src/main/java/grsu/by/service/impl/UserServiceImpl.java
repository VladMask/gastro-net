package grsu.by.service.impl;

import grsu.by.UserRestClient;
import grsu.by.dto.UserShortDto;
import grsu.by.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRestClient userRestClient;
    @Override
    public UserShortDto findById(Long id) {
        return userRestClient.findById(id);
    }
}
