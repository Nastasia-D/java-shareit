package ru.practicum.shareit;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingOutDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class BookingMapperTest {
    @Test
    void mapToOutBookingDto_WhenBookingIsNull() {
        assertNull(BookingMapper.mapToOutBookingDto(null));
    }

    @Test
    void mapToOutBookingDto_WhenValid() {
        User booker = new User();
        booker.setId(2L);

        Item item = new Item();
        item.setId(1L);
        item.setName("Вещь");

        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(2);

        Booking booking = new Booking();
        booking.setId(10L);
        booking.setStart(start);
        booking.setEnd(end);
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setStatus(BookingStatus.WAITING);

        BookingOutDto dto = BookingMapper.mapToOutBookingDto(booking);

        assertNotNull(dto);
        assertEquals(10L, dto.getId());
        assertEquals(start, dto.getStart());
        assertEquals(end, dto.getEnd());
        assertEquals(BookingStatus.WAITING, dto.getStatus());
        assertNotNull(dto.getItem());
        assertEquals(1L, dto.getItem().getId());
        assertEquals("Вещь", dto.getItem().getName());
        assertNotNull(dto.getBooker());
        assertEquals(2L, dto.getBooker().getId());
    }

    @Test
    void mapToOutBookingDto_WhenItemAndBookerAreNull() {
        Booking booking = new Booking();
        booking.setId(10L);
        booking.setItem(null);
        booking.setBooker(null);

        BookingOutDto dto = BookingMapper.mapToOutBookingDto(booking);

        assertNotNull(dto);
        assertEquals(10L, dto.getId());
        assertNull(dto.getItem());
        assertNull(dto.getBooker());
    }

    @Test
    void mapToBooking_WhenBookingDtoIsNull() {
        assertNull(BookingMapper.mapToBooking(null, new Item(), new User()));
    }

    @Test
    void mapToBooking_WhenValid() {
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(2);

        BookingDto dto = new BookingDto();
        dto.setStart(start);
        dto.setEnd(end);

        Item item = new Item();
        item.setId(1L);

        User booker = new User();
        booker.setId(2L);

        Booking booking = BookingMapper.mapToBooking(dto, item, booker);

        assertNotNull(booking);
        assertEquals(start, booking.getStart());
        assertEquals(end, booking.getEnd());
        assertEquals(item, booking.getItem());
        assertEquals(booker, booking.getBooker());
        assertEquals(BookingStatus.WAITING, booking.getStatus());
    }

    @Test
    void mapToBookingDto_WhenBookingIsNull() {
        assertNull(BookingMapper.mapToBookingDto(null));
    }

    @Test
    void mapToBookingDto_WhenValid() {
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(2);

        User booker = new User();
        booker.setId(2L);

        Item item = new Item();
        item.setId(1L);

        Booking booking = new Booking();
        booking.setId(10L);
        booking.setStart(start);
        booking.setEnd(end);
        booking.setBooker(booker);
        booking.setItem(item);

        BookingDto dto = BookingMapper.mapToBookingDto(booking);

        assertNotNull(dto);
        assertEquals(10L, dto.getId());
        assertEquals(start, dto.getStart());
        assertEquals(end, dto.getEnd());
        assertEquals(2L, dto.getBookerId());
        assertEquals(1L, dto.getItemId());
    }

    @Test
    void mapToBookingDto_WhenBookerAndItemAreNull() {
        Booking booking = new Booking();
        booking.setId(10L);
        booking.setBooker(null);
        booking.setItem(null);

        BookingDto dto = BookingMapper.mapToBookingDto(booking);

        assertNotNull(dto);
        assertEquals(10L, dto.getId());
        assertNull(dto.getBookerId());
        assertNull(dto.getItemId());
    }
}
