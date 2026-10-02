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
public class ItemRequestDtoTest {

    @Autowired
    private JacksonTester<ItemRequestDto> json;
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void testItemRequestDto() throws Exception {

        ItemRequestDto itemRequestDto = new ItemRequestDto("Ищу стремянку на 2 дня");

        JsonContent<ItemRequestDto> result = json.write(itemRequestDto);

        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("Ищу стремянку на 2 дня");
    }

    @Test
    void testItemRequestDtoValidation_WhenDescriptionIsBlank() {

        ItemRequestDto itemRequestDto = new ItemRequestDto(" ");

        Set<ConstraintViolation<ItemRequestDto>> violations = validator.validate(itemRequestDto);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("Описание не может быть пустым");
    }
}
