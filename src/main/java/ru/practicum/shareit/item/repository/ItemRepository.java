package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemRepository {
    Item findById(Long itemId);

    Item create(Item item);

    Collection<Item> findAll(Long ownerId);

    Collection<Item> searchItems(String text);

    Item update(Item item);
}
