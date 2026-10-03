package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.ItemShortDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestOutDto;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public ItemRequestOutDto findById(Long requestId, Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        }

        ItemRequest itemRequest = itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Запрос c id: " + requestId + " не найден"));

        // Находим вещи, привязанные к этому requestId
        List<Item> items = itemRepository.findAllByRequestId(requestId);
        List<ItemShortDto> itemDtos = ItemMapper.mapToItemShortDtoList(items);

        return ItemRequestMapper.mapToOutItemRequestDto(itemRequest, itemDtos);
    }

    @Override
    @Transactional
    public ItemRequestOutDto create(ItemRequestDto itemRequestDto, Long userId) {
        User requestor = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id " + userId + " не найден"));

        ItemRequest itemRequest = ItemRequestMapper.toItemRequest(itemRequestDto, requestor);
        itemRequest.setCreated(LocalDateTime.now());
        ItemRequest savedItemRequest = itemRequestRepository.save(itemRequest);
        return ItemRequestMapper.mapToOutItemRequestDto(savedItemRequest, Collections.emptyList());
    }

    @Override
    public List<ItemRequestOutDto> getUserRequests(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        }

        List<ItemRequest> requests = itemRequestRepository.findAllByRequestorId(userId);
        return addItemsToRequests(requests);
    }

    private List<ItemRequestOutDto> addItemsToRequests(List<ItemRequest> requests) {
        if (requests == null || requests.isEmpty()) {
            return Collections.emptyList();
        }

        List<Long> requestIds = requests.stream()
                .map(ItemRequest::getId)
                .collect(Collectors.toList());

        List<Item> items = itemRepository.findAllByRequestIdIn(requestIds);

        Map<Long, List<ItemShortDto>> itemsByRequestId = new HashMap<>();

        for (Item item : items) {
            Long requestId = item.getRequestId();
            if (requestId != null) {
                ItemShortDto dto = ItemMapper.mapToItemShortDto(item);

                itemsByRequestId.computeIfAbsent(requestId, k -> new ArrayList<>()).add(dto);
            }
        }

        return requests.stream()
                .map(req -> ItemRequestMapper.mapToOutItemRequestDto(
                        req,
                        itemsByRequestId.getOrDefault(req.getId(), Collections.emptyList())
                ))
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemRequestOutDto> getAllRequests(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        }

        List<ItemRequest> requests = itemRequestRepository.findAllByRequestorIdNot(userId);

        return addItemsToRequests(requests);
    }

}
