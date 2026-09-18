package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    public ItemDto findById(Long itemId) {
        Item item = itemRepository.findById(itemId);
        return ItemMapper.mapToItemDto(item);
    }

    public ItemDto create(ItemDto itemDto, Long userId) {
        User owner = userRepository.findById(userId);
        Item item = ItemMapper.mapToItem(itemDto, owner);
        Item createItem = itemRepository.create(item);
        return ItemMapper.mapToItemDto(createItem);
    }

    public Collection<ItemDto> findAll(Long ownerId) {
        return itemRepository.findAll(ownerId).stream()
                .map(item -> ItemMapper.mapToItemDto(item))
                .collect(Collectors.toList());
    }

    public Collection<ItemDto> searchItems(String text) {
        if (text == null || text.isBlank()) {
            return List.of();
        }
        return itemRepository.searchItems(text).stream()
                .map(item -> ItemMapper.mapToItemDto(item))
                .collect(Collectors.toList());
    }

    public ItemDto update(Long userId, Long itemId, ItemDto itemDto) {
        Item item = itemRepository.findById(itemId);

        if (!item.getOwner().getId().equals(userId)) {
            throw new NotFoundException("Пользователь с id " + userId + " не является владельцем вещи с id " + itemId);
        }

        if (itemDto.getName() != null && !itemDto.getName().isBlank()) {
            item.setName(itemDto.getName());
        }

        if (itemDto.getDescription() != null && !itemDto.getDescription().isBlank()) {
            item.setDescription(itemDto.getDescription());
        }

        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }

        Item updatedItem = itemRepository.update(item);
        return ItemMapper.mapToItemDto(updatedItem);
    }
}
