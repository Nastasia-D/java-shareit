package ru.practicum.shareit;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.client.ItemRequestClient;

/**
 * TODO Sprint add-item-requests.
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestControllerGateway {

    private final ItemRequestClient itemRequestClient;

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> findById(@PathVariable Long requestId, @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Получен запрос от пользователя {} на получение запроса с id: {} ", userId, requestId);
        return itemRequestClient.findById(requestId, userId);
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody ItemRequestDto itemRequestDto, @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Создан запрос вещи с id: {} от пользователя: {}", itemRequestDto, userId);
        return itemRequestClient.create(itemRequestDto, userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllRequests(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Создан запрос от пользователя {} на получение списка всех запросов других пользователей", userId);
        return itemRequestClient.getAllRequests(userId);
    }

    @GetMapping
    public ResponseEntity<Object> getUserRequests(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Создан запрос от пользователя {} на получение списка свиох запросов", userId);
        return itemRequestClient.getUserRequests(userId);
    }
}
