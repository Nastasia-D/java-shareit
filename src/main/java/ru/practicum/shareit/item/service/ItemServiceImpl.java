package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.comment.CommentMapper;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.dto.CommentOutDto;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.comment.repository.CommentRepository;
import ru.practicum.shareit.item.dto.ItemBookingDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    @Override
    public ItemBookingDto findById(Long itemId, Long userId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь с id " + itemId + " не найдена"));
        List<Comment> comments = commentRepository.findByItemIdOrderByCreatedDesc(itemId);

        BookingDto lastBooking = null;
        BookingDto nextBooking = null;

        if (item.getOwner().getId().equals(userId)) {
            LocalDateTime now = LocalDateTime.now();

            List<Booking> approvedBookings = bookingRepository.findByItemIdAndStatus(itemId, BookingStatus.APPROVED);
            lastBooking = approvedBookings.stream()
                    .filter(b -> !b.getStart().isAfter(now))
                    .max(Comparator.comparing(Booking::getStart))
                    .map(BookingMapper::mapToBookingDto)
                    .orElse(null);

            nextBooking = approvedBookings.stream()
                    .filter(b -> b.getStart().isAfter(now))
                    .findFirst()
                    .map(BookingMapper::mapToBookingDto)
                    .orElse(null);
        }
        return ItemMapper.mapToItemBookingDto(item, lastBooking, nextBooking, comments);
    }

    @Override
    @Transactional
    public ItemDto create(ItemDto itemDto, Long userId) {
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id " + userId + " не найден"));
        Item item = ItemMapper.mapToItem(itemDto, owner);
        item.setOwner(owner);
        Item createItem = itemRepository.save(item);
        return ItemMapper.mapToItemDto(createItem);
    }

    @Override
    public Collection<ItemDto> findAll(Long ownerId) {
        return itemRepository.findByOwnerId(ownerId).stream()
                .map(item -> ItemMapper.mapToItemDto(item))
                .collect(Collectors.toList());
    }

    @Override
    public Collection<ItemDto> searchItems(String text) {
        if (text == null || text.isBlank()) {
            return List.of();
        }
        return itemRepository.searchItems(text).stream()
                .map(item -> ItemMapper.mapToItemDto(item))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ItemDto update(Long userId, Long itemId, ItemDto itemDto) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь с id " + itemId + " не найден"));

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

        Item updatedItem = itemRepository.save(item);
        return ItemMapper.mapToItemDto(updatedItem);
    }

    @Override
    @Transactional
    public CommentOutDto addComment(Long userId, Long itemId, CommentDto commentDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id " + userId + " не найден"));

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь с id " + itemId + " не найден"));

        List<Booking> bookings = bookingRepository.findCompletedBookings(
                userId, itemId, BookingStatus.APPROVED, LocalDateTime.now());

        if (bookings.isEmpty()) {
            throw new ValidationException("Пользователь не арендовал эту вещь или срок аренды ещё не истёк");
        }
        Comment comment = CommentMapper.mapToComment(commentDto, item, user);
        comment.setCreated(LocalDateTime.now());
        Comment savedComment = commentRepository.save(comment);
        return CommentMapper.mapToOutCommentDto(savedComment);

    }

    @Override
    public Collection<ItemBookingDto> getItemsByOwner(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id " + userId + " не найден"));
        List<Item> items = itemRepository.findByOwnerId(userId);
        LocalDateTime now = LocalDateTime.now();
        return items.stream()
                .map(item -> {
                    List<Comment> comments = commentRepository.findByItemIdOrderByCreatedDesc(item.getId());

                    List<Booking> bookings = bookingRepository.findByItemIdAndStatus(item.getId(), BookingStatus.APPROVED);

                    BookingDto lastBooking = bookings.stream()
                            .filter(b -> !b.getStart().isAfter(now))
                            .max(Comparator.comparing(Booking::getStart))
                            .map(BookingMapper::mapToBookingDto)
                            .orElse(null);

                    BookingDto nextBooking = bookings.stream()
                            .filter(b -> b.getStart().isAfter(now))
                            .findFirst()
                            .map(BookingMapper::mapToBookingDto)
                            .orElse(null);

                    return ItemMapper.mapToItemBookingDto(item, lastBooking, nextBooking, comments);
                }).collect(Collectors.toList());
    }

}
