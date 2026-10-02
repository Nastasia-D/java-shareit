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
public class ItemDtoTest {

    @Autowired
    private JacksonTester<ItemDto> json;
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void testItemDto() throws Exception {
        ItemDto itemDto = new ItemDto(
                1L,
                "Дрель",
                "Мощная 900Вт",
                true,
                1L,
                2L
        );

        JsonContent<ItemDto> result = json.write(itemDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("Дрель");
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("Мощная 900Вт");
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(true);
        assertThat(result).extractingJsonPathNumberValue("$.ownerId").isEqualTo(1);
        assertThat(result).extractingJsonPathNumberValue("$.requestId").isEqualTo(2);

    }

    @Test
    void testUserDtoValidation_whenNameInvalid() throws Exception {
        ItemDto itemDto = new ItemDto(
                1L,
                " ",
                "Мощная 900Вт",
                true,
                1L,
                2L
        );

        Set<ConstraintViolation<ItemDto>> violations = validator.validate(itemDto);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Имя не может быть пустым");
    }

    @Test
    void testUserDtoValidation_WhenDescriptionInvalid() throws Exception {
        ItemDto itemDto = new ItemDto(
                1L,
                "Дрель",
                " ",
                true,
                1L,
                2L
        );

        Set<ConstraintViolation<ItemDto>> violations = validator.validate(itemDto);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Описание не может быть пустым");
    }

    @Test
    void testItemDtoValidation_whenAvailableIsNull() {
        ItemDto itemDto = new ItemDto(
                1L,
                "Дрель",
                "Мощная 900Вт",
                null,
                1L,
                null);

        Set<ConstraintViolation<ItemDto>> violations = validator.validate(itemDto);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Статус не может быть пустым/равным null");
    }

}
