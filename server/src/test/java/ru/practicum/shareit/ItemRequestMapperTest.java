package ru.practicum.shareit;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.ItemShortDto;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestMapper;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestOutDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ItemRequestMapperTest {
    @Test
    void mapToOutItemRequestDto_WhenItemRequestIsNull() {
        ItemRequestOutDto result = ItemRequestMapper.mapToOutItemRequestDto(null, Collections.emptyList());
        assertNull(result);
    }

    @Test
    void mapToOutItemRequestDto_WhenItemsIsNull() {
        ItemRequest request = new ItemRequest();
        request.setId(1L);
        request.setDescription("Test description");
        request.setCreated(LocalDateTime.now());

        ItemRequestOutDto result = ItemRequestMapper.mapToOutItemRequestDto(request, null);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test description", result.getDescription());
        assertNotNull(result.getItems());
        assertTrue(result.getItems().isEmpty());
    }

    @Test
    void mapToOutItemRequestDto_WhenValid() {
        ItemRequest request = new ItemRequest();
        request.setId(1L);
        request.setDescription("Test description");
        request.setCreated(LocalDateTime.now());

        ItemShortDto itemShortDto = new ItemShortDto();
        itemShortDto.setId(1L);
        itemShortDto.setName("Item");

        List<ItemShortDto> items = List.of(itemShortDto);

        ItemRequestOutDto result = ItemRequestMapper.mapToOutItemRequestDto(request, items);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test description", result.getDescription());
        assertEquals(1, result.getItems().size());
        assertEquals("Item", result.getItems().get(0).getName());
    }

    @Test
    void toItemRequest_WhenValid() {
        ItemRequestDto dto = new ItemRequestDto();
        dto.setDescription("New request");

        User requester = new User();
        requester.setId(1L);
        requester.setName("Test Name");
        requester.setEmail("test@test.com");

        ItemRequest result = ItemRequestMapper.toItemRequest(dto, requester);

        assertNotNull(result);
        assertEquals("New request", result.getDescription());
        assertEquals(requester, result.getRequestor());
        assertNotNull(result.getCreated());
    }
}
