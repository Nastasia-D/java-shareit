package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestOutDto;

import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {

    private final ItemRequestService itemRequestService;

    @GetMapping("/{requestId}")
    public ItemRequestOutDto findById(@PathVariable Long requestId, @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Получен запрос от пользователя {} на получение запроса с id: {} ", userId, requestId);
        return itemRequestService.findById(requestId, userId);
    }

    @PostMapping
    public ItemRequestOutDto create(@RequestBody ItemRequestDto itemRequestDto, @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Создан запрос вещи с id: {} от пользователя: {}", itemRequestDto, userId);
        return itemRequestService.create(itemRequestDto, userId);
    }

    @GetMapping("/all")
    public List<ItemRequestOutDto> getAllRequests(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Создан запрос от пользователя {} на получение списка всех запросов других пользователей", userId);
        return itemRequestService.getAllRequests(userId);
    }

    @GetMapping
    public List<ItemRequestOutDto> getUserRequests(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Создан запрос от пользователя {} на получение списка свиох запросов", userId);
        return itemRequestService.getUserRequests(userId);
    }
}
