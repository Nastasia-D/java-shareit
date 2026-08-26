package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.Collection;

/**
 * TODO Sprint add-controllers.
 */

@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @GetMapping("/{itemId}")
    public ItemDto findById(@PathVariable Long itemId, @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Получен запрос пользователя {} на получение вещи с id: {}", userId, itemId);
        return itemService.findById(itemId);
    }

    @PostMapping
    public ItemDto create(@Valid @RequestBody ItemDto itemDto, @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Получен запрос на создание вещи: {} от пользователя {}", itemDto.getName(), userId);
        return itemService.create(itemDto, userId);
    }

    @GetMapping
    public Collection<ItemDto> findAll(@RequestHeader("X-Sharer-User-Id") Long ownerId) {
        log.info("Получен запрос на получение списка всех вещей пользователя {}", ownerId);
        return itemService.findAll(ownerId);
    }

    @GetMapping("/search")
    public Collection<ItemDto> searchItems(@RequestParam String text, @RequestHeader("X-Sharer-User-Id") Long ownerId) {
        log.info("Получен запрос на поиск вещи по тексту: {}", text);
        return itemService.searchItems(text);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestBody ItemDto itemDto, @PathVariable Long itemId, @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Получен запрос от пользователя {} на обновление параметров вещи с id: {}", userId, itemId);
        return itemService.update(userId, itemId, itemDto);
    }
}
