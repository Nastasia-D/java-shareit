package ru.practicum.shareit;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.dto.ItemBookingDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemShortDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ItemMapperTest {

    @Test
    void mapToItemDto_WhenItemIsNull() {
        assertNull(ItemMapper.mapToItemDto(null));
    }

    @Test
    void mapToItemDto_WhenOwnerIsNull() {
        Item item = new Item();
        item.setId(1L);
        item.setName("Дрель");
        item.setDescription("Мощная дрель");
        item.setAvailable(true);
        item.setOwner(null);
        item.setRequestId(10L);

        ItemDto dto = ItemMapper.mapToItemDto(item);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("Дрель", dto.getName());
        assertEquals("Мощная дрель", dto.getDescription());
        assertTrue(dto.getAvailable());
        assertNull(dto.getOwnerId());
        assertEquals(10L, dto.getRequestId());
    }

    @Test
    void mapToItemDto_WhenOwnerNotNull() {
        User owner = new User();
        owner.setId(5L);

        Item item = new Item();
        item.setId(1L);
        item.setName("Дрель");
        item.setOwner(owner);

        ItemDto dto = ItemMapper.mapToItemDto(item);

        assertNotNull(dto);
        assertEquals(5L, dto.getOwnerId());
    }

    @Test
    void mapToItem_WhenItemDtoIsNull() {
        assertNull(ItemMapper.mapToItem(null, new User()));
    }

    @Test
    void mapToItem_WhenValid() {
        ItemDto dto = new ItemDto();
        dto.setId(1L);
        dto.setName("Дрель");
        dto.setDescription("Описание");
        dto.setAvailable(true);
        dto.setRequestId(2L);

        User owner = new User();
        owner.setId(3L);

        Item item = ItemMapper.mapToItem(dto, owner);

        assertNotNull(item);
        assertEquals(1L, item.getId());
        assertEquals("Дрель", item.getName());
        assertEquals("Описание", item.getDescription());
        assertTrue(item.getAvailable());
        assertEquals(owner, item.getOwner());
        assertEquals(2L, item.getRequestId());
    }

    @Test
    void mapToItemBookingDto_WhenItemIsNull() {
        assertNull(ItemMapper.mapToItemBookingDto(null, null, null, null));
    }

    @Test
    void mapToItemBookingDto_WhenCommentsIsNull() {
        Item item = new Item();
        item.setId(1L);
        item.setName("Вещь");
        item.setAvailable(true);

        BookingDto last = new BookingDto();
        BookingDto next = new BookingDto();

        ItemBookingDto result = ItemMapper.mapToItemBookingDto(item, last, next, null);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(last, result.getLastBooking());
        assertEquals(next, result.getNextBooking());
        assertNotNull(result.getComments());
        assertTrue(result.getComments().isEmpty());
    }

    @Test
    void mapToItemBookingDto_WhenCommentsArePresent() {
        Item item = new Item();
        item.setId(1L);

        User author = new User();
        author.setId(1L);
        author.setName("Author Name");

        Comment comment = new Comment();
        comment.setId(1L);
        comment.setText("Отличная вещь!");
        comment.setCreated(LocalDateTime.now());
        comment.setAuthor(author);

        ItemBookingDto result = ItemMapper.mapToItemBookingDto(item, null, null, List.of(comment));

        assertNotNull(result);
        assertNotNull(result.getComments());
        assertEquals(1, result.getComments().size());
        assertEquals("Отличная вещь!", result.getComments().get(0).getText());
    }

    @Test
    void mapToItemShortDto_WhenItemIsNull() {
        assertNull(ItemMapper.mapToItemShortDto(null));
    }

    @Test
    void mapToItemShortDto_WhenValid() {
        Item item = new Item();
        item.setId(1L);
        item.setName("Короткое имя");

        ItemShortDto dto = ItemMapper.mapToItemShortDto(item);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("Короткое имя", dto.getName());
    }

    @Test
    void mapToItemShortDtoList_WhenListIsNull() {
        assertTrue(ItemMapper.mapToItemShortDtoList(null).isEmpty());
    }

    @Test
    void mapToItemShortDtoList_WhenListIsEmpty() {
        assertTrue(ItemMapper.mapToItemShortDtoList(Collections.emptyList()).isEmpty());
    }

    @Test
    void mapToItemShortDtoList_WhenValid() {
        Item item = new Item();
        item.setId(1L);
        item.setName("Вещь");

        List<ItemShortDto> list = ItemMapper.mapToItemShortDtoList(List.of(item));

        assertNotNull(list);
        assertEquals(1, list.size());
        assertEquals("Вещь", list.get(0).getName());
    }
}
