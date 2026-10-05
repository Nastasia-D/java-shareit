package ru.practicum.shareit.request;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.dto.ItemShortDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestOutDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemRequestMapper {

    public static ItemRequestOutDto mapToOutItemRequestDto(ItemRequest itemRequest, List<ItemShortDto> items) {
        if (itemRequest == null) {
            return null;
        }
        ItemRequestOutDto dto = new ItemRequestOutDto();
        dto.setId(itemRequest.getId());
        dto.setDescription(itemRequest.getDescription());
        dto.setCreated(itemRequest.getCreated());
        dto.setItems(items != null ? items : Collections.emptyList());
        return dto;

    }

    public static ItemRequest toItemRequest(ItemRequestDto dto, User requester) {
        ItemRequest request = new ItemRequest();
        request.setDescription(dto.getDescription());
        request.setRequestor(requester);
        request.setCreated(LocalDateTime.now());
        return request;
    }
}
