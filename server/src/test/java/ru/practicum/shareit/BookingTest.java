package ru.practicum.shareit;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class BookingTest {
    @Test
    void testBookingGettersAndSetters() {
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(2);
        Item item = new Item();
        item.setId(1L);
        User booker = new User();
        booker.setId(2L);

        Booking booking = new Booking();
        booking.setId(10L);
        booking.setStart(start);
        booking.setEnd(end);
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setStatus(BookingStatus.APPROVED);

        assertThat(booking.getId()).isEqualTo(10L);
        assertThat(booking.getStart()).isEqualTo(start);
        assertThat(booking.getEnd()).isEqualTo(end);
        assertThat(booking.getItem()).isEqualTo(item);
        assertThat(booking.getBooker()).isEqualTo(booker);
        assertThat(booking.getStatus()).isEqualTo(BookingStatus.APPROVED);
        assertThat(booking.toString()).contains("Booking");
    }

    @Test
    void testEqualsAndHashCode() {
        Booking booking1 = new Booking();
        booking1.setId(1L);

        Booking booking2 = new Booking();
        booking2.setId(1L);

        Booking booking3 = new Booking();
        booking3.setId(2L);

        Booking bookingNullId1 = new Booking();
        Booking bookingNullId2 = new Booking();

        assertThat(booking1).isEqualTo(booking1);
        assertThat(booking1).isEqualTo(booking2);
        assertThat(booking1).isNotEqualTo(booking3);
        assertThat(booking1).isNotEqualTo(null);
        assertThat(booking1).isNotEqualTo("some string");
        assertThat(bookingNullId1).isNotEqualTo(booking2);
        assertThat(bookingNullId1).isNotEqualTo(bookingNullId2);

        assertThat(booking1.hashCode()).isEqualTo(booking2.hashCode());
        assertThat(bookingNullId1.hashCode()).isEqualTo(bookingNullId2.hashCode());
    }
}
