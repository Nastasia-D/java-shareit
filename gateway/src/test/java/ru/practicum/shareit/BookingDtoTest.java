package ru.practicum.shareit;


import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@ContextConfiguration(classes = ShareItGateway.class)
public class BookingDtoTest {

    @Autowired
    private JacksonTester<BookingDto> json;

    private Validator validator;

    @BeforeEach
    void setUp() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    void testBookingDto() throws Exception {
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(2);

        BookingDto bookingDto = new BookingDto(
                1L,
                1L,
                2L,
                start,
                end
        );

        JsonContent<BookingDto> result = json.write(bookingDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathNumberValue("$.bookerId").isEqualTo(1);
        assertThat(result).extractingJsonPathNumberValue("$.itemId").isEqualTo(2);
        assertThat(result).extractingJsonPathStringValue("$.start").isNotNull();
        assertThat(result).extractingJsonPathStringValue("$.end").isNotNull();
    }

    @Test
    void testBookingDtoValidation_WhereStartInvalid() throws Exception {
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(2);

        BookingDto bookingDto = new BookingDto(
                1L,
                1L,
                2L,
                null,
                end
        );

        Set<ConstraintViolation<BookingDto>> violations = validator.validate(bookingDto);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Время начала бронирования не может быть пустым");
    }

    @Test
    void testBookingDtoValidation_WhereEndInvalid() throws Exception {
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(2);

        BookingDto bookingDto = new BookingDto(
                1L,
                1L,
                2L,
                start,
                null
        );

        Set<ConstraintViolation<BookingDto>> violations = validator.validate(bookingDto);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Время окончания бронирования не может быть пустым");
    }

    @Test
    void testBookingDtoValidation_whenStartInPast() {
        LocalDateTime startInPast = LocalDateTime.now().minusDays(1);
        LocalDateTime endInFuture = LocalDateTime.now().plusDays(1);

        BookingDto bookingDto = new BookingDto(
                1L,
                1L,
                2L,
                startInPast,
                endInFuture
        );

        Set<ConstraintViolation<BookingDto>> violations = validator.validate(bookingDto);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("Время начала бронирования не может быть в прошлом");
    }

    @Test
    void testBookingDtoValidation_whenEndInPast() {
        LocalDateTime startInFuture = LocalDateTime.now().plusDays(1);
        LocalDateTime endInPast = LocalDateTime.now().minusDays(1);

        BookingDto bookingDto = new BookingDto(
                1L,
                1L,
                2L,
                startInFuture,
                endInPast
        );

        Set<ConstraintViolation<BookingDto>> violations = validator.validate(bookingDto);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("Время окончания бронирования должно быть в будущем");
    }
}
