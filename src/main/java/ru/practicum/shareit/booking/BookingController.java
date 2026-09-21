package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingOutDto;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.Collection;

/**
 * TODO Sprint add-bookings.
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingOutDto createBooking(@Valid @RequestBody BookingDto bookingDto, @RequestHeader("X-Sharer-User-Id") Long bookerId) {
        log.info("Получен запрос на создание брони: {} от пользователя {}", bookingDto, bookerId);
        return bookingService.create(bookingDto, bookerId);
    }

    @PatchMapping("/{bookingId}")
    public BookingOutDto approved(@PathVariable Long bookingId, @RequestHeader("X-Sharer-User-Id") Long ownerId, @RequestParam Boolean approved) {
        log.info("Получен запрос на подтверждение (approved={}) бронирования {} от пользователя {}", approved, bookingId, ownerId);
        return bookingService.approved(bookingId, ownerId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingOutDto findById(@PathVariable Long bookingId, @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Получен запрос пользователя {} на просмотр бронирования {}", userId, bookingId);
        return bookingService.findById(bookingId, userId);
    }

    @GetMapping
    public Collection<BookingOutDto> getUserBookings(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestParam(defaultValue = "ALL") BookingState state) {
        log.info("Получен запрос пользователя {} на получение списка бронирований со статусом {}", userId, state);
        return bookingService.getUserBookings(userId, state);
    }

    @GetMapping("/owner")
    public Collection<BookingOutDto> getOwnerBookings(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestParam(defaultValue = "ALL") BookingState state) {
        log.info("Получен запрос владельца {} на получение списка бронирований со статусом {}", userId, state);
        return bookingService.getOwnerBookings(userId, state);
    }
}
