package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserDto create(UserDto userDto) {
        User user = UserMapper.mapToUser(userDto);
        User createUser = userRepository.create(user);
        return UserMapper.mapToUserDto(createUser);
    }

    public UserDto update(Long userId, UserDto userDto) {
        User user = UserMapper.mapToUser(userDto);
        User updateUser = userRepository.update(userId, user);
        return UserMapper.mapToUserDto(updateUser);
    }

    public void delete(Long userId) {
        userRepository.delete(userId);
    }

    public UserDto findById(Long userId) {
        User user = userRepository.findById(userId);
        return UserMapper.mapToUserDto(user);
    }

    public Collection<UserDto> findAll() {
        return userRepository.findAll().stream()
                .map(user -> UserMapper.mapToUserDto(user))
                .collect(Collectors.toList());
    }
}
