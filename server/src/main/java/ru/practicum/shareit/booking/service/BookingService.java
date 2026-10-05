package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingOutDto;
import ru.practicum.shareit.booking.model.BookingState;

import java.util.Collection;

public interface BookingService {

    BookingOutDto create(BookingDto bookingDto, Long bookerId);

    BookingOutDto approved(Long bookingId, Long ownerId, Boolean approved);

    BookingOutDto findById(Long bookingId, Long userId);

    Collection<BookingOutDto> getUserBookings(Long userId, BookingState state);

    Collection<BookingOutDto> getOwnerBookings(Long userId, BookingState state);
}
