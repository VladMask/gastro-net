package grsu.by.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import grsu.by.dto.EmailResponse;
import grsu.by.dto.UserCreationDto;
import grsu.by.dto.UserFullDto;
import grsu.by.dto.UserShortDto;
import grsu.by.dto.UserUpdateDto;
import grsu.by.entity.OutboxEvent;
import grsu.by.entity.User;
import grsu.by.repository.UserRepository;
import grsu.by.service.OutboxEventService;
import grsu.by.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final ModelMapper mapper;
    private final UserRepository userRepository;
    private final OutboxEventService outboxEventService;
    private final ObjectMapper objectMapper;

    @SneakyThrows
    @Transactional
    @Override
    public Long create(UserCreationDto creationDto) {
        User user = mapper.map(creationDto, User.class);
        try {
            user = userRepository.save(user);
        }
        catch (DataIntegrityViolationException exception) {
            throw new IllegalStateException("User with specified email already exists");
        }
        outboxEventService.create(new OutboxEvent(
                "User created",
                objectMapper.writeValueAsString(user))
        );
        return user.getId();
    }

    @Transactional(readOnly = true)
    @Override
    public UserShortDto findById(Long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("User not found")
        );
        return mapper.map(user, UserShortDto.class);
    }

    @Override
    public EmailResponse findUserEmail(Long userId) {
        String email = userRepository.findUserEmailByUserId(userId).orElseThrow(
                () -> new EntityNotFoundException("User not found")
        );
        return new EmailResponse(email);
    }

    @Override
    public UserFullDto findByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new EntityNotFoundException("User not found")
        );
        return mapper.map(user, UserFullDto.class);
    }

    @Transactional
    public UserFullDto updateMe(String email, UserUpdateDto dto) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        if (dto.getFirstname() != null) user.setFirstname(dto.getFirstname());
        if (dto.getLastname() != null) user.setLastname(dto.getLastname());
        if (dto.getPhoneNumber() != null) user.setPhoneNumber(dto.getPhoneNumber());
        if (dto.getBirthDate() != null) user.setBirthDate(dto.getBirthDate());
        return mapper.map(userRepository.save(user), UserFullDto.class);
    }

}
