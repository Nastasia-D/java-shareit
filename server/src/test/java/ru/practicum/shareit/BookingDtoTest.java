package ru.practicum.shareit;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingOutDto;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.ItemShortDto;
import ru.practicum.shareit.user.dto.UserShortDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class BookingDtoTest {
    @Test
    void testBookingDto() {
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(2);

        BookingDto dto = new BookingDto(1L, 2L, 3L, start, end);

        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getBookerId()).isEqualTo(2L);
        assertThat(dto.getItemId()).isEqualTo(3L);
        assertThat(dto.getStart()).isEqualTo(start);
        assertThat(dto.getEnd()).isEqualTo(end);

        BookingDto emptyDto = new BookingDto();
        emptyDto.setId(10L);
        emptyDto.setBookerId(20L);
        emptyDto.setItemId(30L);
        emptyDto.setStart(start);
        emptyDto.setEnd(end);

        assertThat(emptyDto.getId()).isEqualTo(10L);
        assertThat(emptyDto.getBookerId()).isEqualTo(20L);
        assertThat(emptyDto.getItemId()).isEqualTo(30L);
        assertThat(emptyDto.getStart()).isEqualTo(start);
        assertThat(emptyDto.getEnd()).isEqualTo(end);
    }

    @Test
    void testBookingOutDto() {
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(2);
        ItemShortDto item = new ItemShortDto();
        item.setId(1L);
        item.setName("Item Name");

        UserShortDto booker = new UserShortDto();
        booker.setId(2L);

        BookingOutDto dto = new BookingOutDto(1L, start, end, item, booker, BookingStatus.APPROVED);

        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getStart()).isEqualTo(start);
        assertThat(dto.getEnd()).isEqualTo(end);
        assertThat(dto.getItem()).isEqualTo(item);
        assertThat(dto.getBooker()).isEqualTo(booker);
        assertThat(dto.getStatus()).isEqualTo(BookingStatus.APPROVED);

        BookingOutDto emptyDto = new BookingOutDto();
        emptyDto.setId(5L);
        emptyDto.setStart(start);
        emptyDto.setEnd(end);
        emptyDto.setItem(item);
        emptyDto.setBooker(booker);
        emptyDto.setStatus(BookingStatus.REJECTED);

        assertThat(emptyDto.getId()).isEqualTo(5L);
        assertThat(emptyDto.getStatus()).isEqualTo(BookingStatus.REJECTED);
    }
}
