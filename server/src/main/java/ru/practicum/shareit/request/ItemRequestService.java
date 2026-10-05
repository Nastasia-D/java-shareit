package ru.practicum.shareit.request;


import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestOutDto;

import java.util.List;

public interface ItemRequestService {
    ItemRequestOutDto findById(Long requestId, Long userId);

    ItemRequestOutDto create(ItemRequestDto itemRequestDto, Long userId);

    List<ItemRequestOutDto> getUserRequests(Long userId);

    List<ItemRequestOutDto> getAllRequests(Long userId);
}
