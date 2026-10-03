package ru.practicum.shareit;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@ContextConfiguration(classes = ShareItGateway.class)
@Import(LocalValidatorFactoryBean.class)
public class CommentDtoTest {
    @Autowired
    private JacksonTester<CommentDto> json;

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void testCommentDto() throws Exception {

        CommentDto commentDto = new CommentDto(
                "Отличная дрель"
        );

        JsonContent<CommentDto> result = json.write(commentDto);

        assertThat(result).extractingJsonPathStringValue("$.text").isEqualTo("Отличная дрель");
    }

    @Test
    void testCommentDtoValidation_WhenTextIsBlank() {
        CommentDto commentDto = new CommentDto("   ");

        Set<ConstraintViolation<CommentDto>> violations = validator.validate(commentDto);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("Текст комментария не может быть пустым");
    }
}
