package ru.practicum.shareit;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import static org.junit.jupiter.api.Assertions.*;

public class UserMapperTest {
    @Test
    void mapToUserDto() {
        User user = new User();
        user.setId(1L);
        user.setName("Test Name");
        user.setEmail("test@test.com");

        UserDto dto = UserMapper.mapToUserDto(user);

        assertNotNull(dto);
        assertEquals(user.getId(), dto.getId());
        assertEquals(user.getName(), dto.getName());
        assertEquals(user.getEmail(), dto.getEmail());
    }

    @Test
    void mapToUserDtoWhenUserIsNull() {
        UserDto dto = UserMapper.mapToUserDto(null);
        assertNull(dto);
    }

    @Test
    void mapToUser() {
        UserDto dto = new UserDto(1L, "Test Name", "test@test.com");

        User user = UserMapper.mapToUser(dto);

        assertNotNull(user);
        assertEquals(dto.getId(), user.getId());
        assertEquals(dto.getName(), user.getName());
        assertEquals(dto.getEmail(), user.getEmail());
    }

    @Test
    void mapToUserWhenDtoIsNull() {
        User user = UserMapper.mapToUser(null);
        assertNull(user);
    }
}
