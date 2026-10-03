package ru.practicum.shareit;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.comment.dto.CommentOutDto;
import ru.practicum.shareit.item.dto.ItemBookingDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemShortDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ItemDtoTest {
    @Test
    void testItemShortDto() {
        ItemShortDto dto = new ItemShortDto(1L, "Дрель");

        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getName()).isEqualTo("Дрель");

        ItemShortDto emptyDto = new ItemShortDto();
        emptyDto.setId(2L);
        emptyDto.setName("Шуруповерт");

        assertThat(emptyDto.getId()).isEqualTo(2L);
        assertThat(emptyDto.getName()).isEqualTo("Шуруповерт");
    }

    @Test
    void testItemDto() {
        ItemDto dto = new ItemDto(1L, "Дрель", "Мощная дрель", true, 2L, 3L);

        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getName()).isEqualTo("Дрель");
        assertThat(dto.getDescription()).isEqualTo("Мощная дрель");
        assertThat(dto.getAvailable()).isTrue();
        assertThat(dto.getOwnerId()).isEqualTo(2L);
        assertThat(dto.getRequestId()).isEqualTo(3L);

        ItemDto emptyDto = new ItemDto();
        emptyDto.setId(4L);
        emptyDto.setName("Молоток");
        emptyDto.setDescription("Обычный молоток");
        emptyDto.setAvailable(false);
        emptyDto.setOwnerId(5L);
        emptyDto.setRequestId(6L);

        assertThat(emptyDto.getId()).isEqualTo(4L);
        assertThat(emptyDto.getName()).isEqualTo("Молоток");
        assertThat(emptyDto.getDescription()).isEqualTo("Обычный молоток");
        assertThat(emptyDto.getAvailable()).isFalse();
        assertThat(emptyDto.getOwnerId()).isEqualTo(5L);
        assertThat(emptyDto.getRequestId()).isEqualTo(6L);
    }

    @Test
    void testItemBookingDto() {
        LocalDateTime now = LocalDateTime.now();
        BookingDto lastBooking = new BookingDto(1L, 2L, 1L, now.minusDays(2), now.minusDays(1));
        BookingDto nextBooking = new BookingDto(2L, 3L, 1L, now.plusDays(1), now.plusDays(2));
        CommentOutDto comment = new CommentOutDto();
        comment.setId(1L);
        comment.setText("Отличная вещь!");
        List<CommentOutDto> comments = List.of(comment);

        ItemBookingDto dto = new ItemBookingDto(
                1L, "Дрель", "Описание", true, 2L, 3L, lastBooking, nextBooking, comments
        );

        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getName()).isEqualTo("Дрель");
        assertThat(dto.getDescription()).isEqualTo("Описание");
        assertThat(dto.getAvailable()).isTrue();
        assertThat(dto.getOwnerId()).isEqualTo(2L);
        assertThat(dto.getRequestId()).isEqualTo(3L);
        assertThat(dto.getLastBooking()).isEqualTo(lastBooking);
        assertThat(dto.getNextBooking()).isEqualTo(nextBooking);
        assertThat(dto.getComments()).isEqualTo(comments);


        ItemBookingDto emptyDto = new ItemBookingDto();
        emptyDto.setId(10L);
        emptyDto.setName("Пила");
        emptyDto.setDescription("Новая пила");
        emptyDto.setAvailable(false);
        emptyDto.setOwnerId(20L);
        emptyDto.setRequestId(30L);
        emptyDto.setLastBooking(lastBooking);
        emptyDto.setNextBooking(nextBooking);
        emptyDto.setComments(comments);

        assertThat(emptyDto.getId()).isEqualTo(10L);
        assertThat(emptyDto.getName()).isEqualTo("Пила");
        assertThat(emptyDto.getLastBooking()).isEqualTo(lastBooking);
        assertThat(emptyDto.getComments()).hasSize(1);
    }
}
