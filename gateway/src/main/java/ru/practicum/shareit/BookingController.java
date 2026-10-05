package ru.practicum.shareit;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.client.BookingClient;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> createBooking(@Valid @RequestBody BookingDto bookingDto, @RequestHeader("X-Sharer-User-Id") Long bookerId) {
        log.info("Получен запрос на создание брони: {} от пользователя {}", bookingDto, bookerId);
        return bookingClient.create(bookingDto, bookerId);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> approved(@PathVariable Long bookingId, @RequestHeader("X-Sharer-User-Id") Long ownerId, @RequestParam Boolean approved) {
        log.info("Получен запрос на подтверждение (approved={}) бронирования {} от пользователя {}", approved, bookingId, ownerId);
        return bookingClient.approved(bookingId, ownerId, approved);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> findById(@PathVariable Long bookingId, @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Получен запрос пользователя {} на просмотр бронирования {}", userId, bookingId);
        return bookingClient.findById(bookingId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getUserBookings(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestParam(defaultValue = "ALL") BookingState state) {
        log.info("Получен запрос пользователя {} на получение списка бронирований со статусом {}", userId, state);
        return bookingClient.getUserBookings(userId, state);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getOwnerBookings(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestParam(defaultValue = "ALL") BookingState state) {
        log.info("Получен запрос владельца {} на получение списка бронирований со статусом {}", userId, state);
        return bookingClient.getOwnerBookings(userId, state);
    }
}
