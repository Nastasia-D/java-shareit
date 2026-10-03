package ru.practicum.shareit.booking;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingOutDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.ItemShortDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserShortDto;
import ru.practicum.shareit.user.model.User;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BookingMapper {
    public static BookingOutDto mapToOutBookingDto(Booking booking) {
        if (booking == null) {
            return null;
        }
        BookingOutDto dto = new BookingOutDto();
        dto.setId(booking.getId());
        dto.setStart(booking.getStart());
        dto.setEnd(booking.getEnd());
        if (booking.getItem() != null) {
            dto.setItem(new ItemShortDto(booking.getItem().getId(), booking.getItem().getName()));
        }
        if (booking.getBooker() != null) {
            dto.setBooker(new UserShortDto(booking.getBooker().getId()));
        }
        dto.setStatus(booking.getStatus());
        return dto;
    }

    public static Booking mapToBooking(BookingDto bookingDto, Item item, User booker) {
        if (bookingDto == null) {
            return null;
        }

        Booking booking = new Booking();
        booking.setStart(bookingDto.getStart());
        booking.setEnd(bookingDto.getEnd());
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setStatus(BookingStatus.WAITING);
        return booking;
    }

    public static BookingDto mapToBookingDto(Booking booking) {
        if (booking == null) {
            return null;
        }
        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(booking.getId());
        bookingDto.setBookerId(booking.getBooker() != null ? booking.getBooker().getId() : null);
        bookingDto.setItemId(booking.getItem() != null ? booking.getItem().getId() : null);
        bookingDto.setStart(booking.getStart());
        bookingDto.setEnd(booking.getEnd());
        return bookingDto;
    }
}
