package ru.practicum.shareit;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserShortDto;

import static org.assertj.core.api.Assertions.assertThat;

public class UserDtoTest {
    @Test
    void testUserShortDto() {

        UserShortDto dto = new UserShortDto(1L);
        assertThat(dto.getId()).isEqualTo(1L);

        UserShortDto emptyDto = new UserShortDto();
        emptyDto.setId(2L);
        assertThat(emptyDto.getId()).isEqualTo(2L);
    }

    @Test
    void testUserDto() {

        UserDto dto = new UserDto(1L, "Иван", "ivan@email.com");

        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getName()).isEqualTo("Иван");
        assertThat(dto.getEmail()).isEqualTo("ivan@email.com");

        UserDto emptyDto = new UserDto();
        emptyDto.setId(2L);
        emptyDto.setName("Петр");
        emptyDto.setEmail("petr@email.com");

        assertThat(emptyDto.getId()).isEqualTo(2L);
        assertThat(emptyDto.getName()).isEqualTo("Петр");
        assertThat(emptyDto.getEmail()).isEqualTo("petr@email.com");
    }
}
