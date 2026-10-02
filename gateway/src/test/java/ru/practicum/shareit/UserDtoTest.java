package ru.practicum.shareit;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.test.context.ContextConfiguration;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@ContextConfiguration(classes = ShareItGateway.class)
public class UserDtoTest {

    @Autowired
    private JacksonTester<UserDto> json;
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void testUserDto() throws Exception {
        UserDto userDto = new UserDto(
                1L,
                "Пользователь_1",
                "Email@email.com"
        );

        JsonContent<UserDto> result = json.write(userDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("Пользователь_1");
        assertThat(result).extractingJsonPathStringValue("$.email").isEqualTo("Email@email.com");
    }

    @Test
    void testUserDtoValidation_whenNameInvalid() throws Exception {
        UserDto userDto = new UserDto(
                1L,
                " ",
                "Email@email.com"
        );

        Set<ConstraintViolation<UserDto>> violations = validator.validate(userDto);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Имя не может быть пустым");
    }

    @Test
    void testUserDtoValidation_whenEmailInvalid() {
        UserDto userDto = new UserDto(1L, "Пользователь_1", "invalid-email");

        Set<ConstraintViolation<UserDto>> violations = validator.validate(userDto);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Некорректный формат email");
    }
}
