package ru.practicum.shareit.item.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class InMemoryItemRepository implements ItemRepository {
    private final Map<Long, Item> items = new HashMap<>();
    private long idCounter = 0;

    @Override
    public Item findById(Long itemId) {
        if (!items.containsKey(itemId)) {
            throw new NotFoundException("Вещь с id " + itemId + " не найдена");
        }
        return items.get(itemId);
    }

    @Override
    public Item create(Item item) {
        item.setId(++idCounter);
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Collection<Item> findAll(Long ownerId) {
        return items.values().stream()
                .filter(item -> item.getOwner() != null && item.getOwner().getId().equals(ownerId))
                .collect((Collectors.toList()));
    }

    @Override
    public Collection<Item> searchItems(String text) {
        return items.values().stream()
                .filter(item -> Boolean.TRUE.equals(item.getAvailable()))
                .filter(item -> (item.getName() != null && item.getName().toLowerCase().contains(text.toLowerCase()))
                        || (item.getDescription() != null && item.getDescription().toLowerCase().contains(text.toLowerCase())))
                .collect(Collectors.toList());
    }

    @Override
    public Item update(Item item) {
        items.put(item.getId(), item);
        return item;
    }
}
