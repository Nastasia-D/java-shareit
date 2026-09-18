package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.dto.CommentOutDto;
import ru.practicum.shareit.item.dto.ItemBookingDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collection;

public interface ItemService {

    ItemBookingDto findById(Long itemId, Long userId);

    ItemDto create(ItemDto itemDto, Long userId);

    Collection<ItemDto> findAll(Long ownerId);

    Collection<ItemDto> searchItems(String text);

    ItemDto update(Long userId, Long itemId, ItemDto itemDto);

    CommentOutDto addComment(Long userId, Long itemId, CommentDto commentDto);

    Collection<ItemBookingDto> getItemsByOwner(Long userId);

}
