package ru.practicum.shareit;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.client.ItemClient;

@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemClient itemClient;

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> findById(@PathVariable Long itemId, @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Получен запрос пользователя {} на получение вещи с id: {}", userId, itemId);
        return itemClient.findById(itemId, userId);
    }

    @PostMapping
    public ResponseEntity<Object> create(@Valid @RequestBody ItemDto itemDto, @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Получен запрос на создание вещи: {} от пользователя {}", itemDto.getName(), userId);
        return itemClient.create(itemDto, userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItems(@RequestParam String text, @RequestHeader("X-Sharer-User-Id") Long ownerId) {
        log.info("Получен запрос на поиск вещи по тексту: {}", text);
        return itemClient.searchItems(text, ownerId);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> update(@PathVariable Long itemId, @RequestHeader("X-Sharer-User-Id") Long userId, @RequestBody ItemDto itemDto) {
        log.info("Получен запрос от пользователя {} на обновление параметров вещи с id: {}", userId, itemId);
        return itemClient.update(itemId, userId, itemDto);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long itemId, @Valid @RequestBody CommentDto commentDto) {
        log.info("Пользователь {} оставил комментарий {} к вещи {}", userId, commentDto, itemId);
        return itemClient.addComment(userId, itemId, commentDto);
    }

    @GetMapping
    public ResponseEntity<Object> getItemsByOwner(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Запрос списка вещей владельца с id {}", userId);
        return itemClient.getItemsByOwner(userId);
    }

}
