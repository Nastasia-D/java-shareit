package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingDto {
    private Long id;
    private Long bookerId;
    private Long itemId;
    @NotNull(message = "Время начала бронирования не может быть пустым")
    private LocalDateTime start;
    @NotNull(message = "Время окончания бронирования не может быть пустым")
    private LocalDateTime end;
}
