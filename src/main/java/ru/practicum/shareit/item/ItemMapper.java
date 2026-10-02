package ru.practicum.shareit.item;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.comment.CommentMapper;
import ru.practicum.shareit.item.comment.dto.CommentOutDto;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.dto.ItemBookingDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemMapper {
    public static ItemDto mapToItemDto(Item item) {
        if (item == null) {
            return null;
        }
        ItemDto dto = new ItemDto();
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setDescription(item.getDescription());
        dto.setAvailable(item.getAvailable());
        dto.setOwnerId(item.getOwner() != null ? item.getOwner().getId() : null);
        return dto;
    }

    public static Item mapToItem(ItemDto itemDto, User owner) {
        if (itemDto == null) {
            return null;
        }
        Item item = new Item();
        item.setId(itemDto.getId());
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable());
        item.setOwner(owner);
        return item;
    }

     public static ItemBookingDto mapToItemBookingDto(Item item, BookingDto lastBooking, BookingDto nextBooking, List<Comment> comments) {
         if (item == null) {
             return null;
         }
         ItemBookingDto itemBookingDto = new ItemBookingDto();
         itemBookingDto.setId(item.getId());
         itemBookingDto.setName(item.getName());
         itemBookingDto.setDescription(item.getDescription());
         itemBookingDto.setAvailable(item.getAvailable());
         itemBookingDto.setOwnerId(item.getOwner() != null ? item.getOwner().getId() : null);
         itemBookingDto.setLastBooking(lastBooking);
         itemBookingDto.setNextBooking(nextBooking);
         List<CommentOutDto> commentOutDtos = comments != null
                 ? comments.stream().map(CommentMapper::mapToOutCommentDto).collect(Collectors.toList())
                 : Collections.emptyList();
         itemBookingDto.setComments(commentOutDtos);
         return itemBookingDto;
     }
}
