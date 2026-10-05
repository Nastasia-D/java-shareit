package ru.practicum.shareit;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingDto {
    private Long id;
    private Long bookerId;
    private Long itemId;
    @NotNull(message = "Время начала бронирования не может быть пустым")
    @FutureOrPresent(message = "Время начала бронирования не может быть в прошлом")
    private LocalDateTime start;

    @NotNull(message = "Время окончания бронирования не может быть пустым")
    @Future(message = "Время окончания бронирования должно быть в будущем")
    private LocalDateTime end;
}
