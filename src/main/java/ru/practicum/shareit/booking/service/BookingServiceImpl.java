package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingOutDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;

    @Override
    @Transactional
    public BookingOutDto create(BookingDto bookingDto, Long bookerId) {
        if (bookingDto.getEnd().isBefore(bookingDto.getStart()) || bookingDto.getEnd().equals(bookingDto.getStart())) {
            throw new ValidationException("Дата окончания бронирования не может быть раньше или равна дате начала");
        }
        User booker = userRepository.findById(bookerId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id " + bookerId + " не найден"));
        Item item = itemRepository.findById(bookingDto.getItemId())
                .orElseThrow(() -> new NotFoundException("Вещь с id " + bookingDto.getItemId() + " не найден"));
        if (item.getOwner().getId().equals(bookerId)) {
            throw new ForbiddenException("Владелец не может забронировать собственную вещь");
        }
        if (!Boolean.TRUE.equals(item.getAvailable())) {
            throw new ValidationException("Вещь с id " + item.getId() + " недоступна для бронирования"); /* оставила ValidationException,
            т.е. автотесты не проходят, указано, что нужна ошибка со статусом 400 */
        }
        Booking booking = BookingMapper.mapToBooking(bookingDto, item, booker);
        Booking createBooking = bookingRepository.save(booking);
        return BookingMapper.mapToOutBookingDto(createBooking);
    }

    @Override
    @Transactional
    public BookingOutDto approved(Long bookingId, Long ownerId, Boolean approved) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронирование с id " + bookingId + " не существует"));

        if (!booking.getItem().getOwner().getId().equals(ownerId)) {
            throw new ForbiddenException("Пользователь с id " + ownerId + " не является владельцем вещи"); // ИИ подстказал, что тут можно использовать ошибку со статусом 403 (ForbiddenException)
        }

        if (!booking.getStatus().equals(BookingStatus.WAITING)) {
            throw new ValidationException("Нельзя изменить статус бронирования, которое не находится в состоянии ожидания (WAITING)");
        }

        if (approved) {
            booking.setStatus(BookingStatus.APPROVED);
        } else {
            booking.setStatus(BookingStatus.REJECTED);
        }

        Booking approvedBooking = bookingRepository.save(booking);
        return BookingMapper.mapToOutBookingDto(approvedBooking);
    }

    @Override
    public BookingOutDto findById(Long bookingId, Long userId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронирование с id " + bookingId + " не существует"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id " + userId + " не найден"));

        if (!booking.getItem().getOwner().getId().equals(userId) && !booking.getBooker().getId().equals(userId)) {
            throw new NotFoundException("Пользователь с id " + userId + " не имеет право просмотреть бронирование");
        }

        return BookingMapper.mapToOutBookingDto(booking);
    }

    @Override
    public Collection<BookingOutDto> getUserBookings(Long userId, BookingState state) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        }

        LocalDateTime now = LocalDateTime.now();
        List<Booking> bookingList;

        switch (state) {
            case ALL ->
                bookingList = bookingRepository.findAllByBooker(userId);
            case PAST ->
                bookingList = bookingRepository.findPastBookings(userId, now);
            case FUTURE ->
                bookingList = bookingRepository.findFutureByBooker(userId, now);
            case CURRENT ->
                bookingList = bookingRepository.findCurrentBookings(userId, now, now);
            case WAITING ->
                bookingList = bookingRepository.findBookingsByBookerAndStatus(userId, BookingStatus.WAITING);
            case REJECTED ->
                bookingList = bookingRepository.findBookingsByBookerAndStatus(userId, BookingStatus.REJECTED);
            default ->
                    throw new IllegalArgumentException("Неверный статус: " + state);
        }

        return bookingList.stream()
                .map(BookingMapper::mapToOutBookingDto)
                .collect(Collectors.toList());
    }

    @Override
    public  Collection<BookingOutDto> getOwnerBookings(Long userId, BookingState state) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        }

        LocalDateTime now = LocalDateTime.now();
        List<Booking> bookingList;

        switch (state) {
            case ALL ->
                    bookingList = bookingRepository.findAllByOwner(userId);
            case PAST ->
                    bookingList = bookingRepository.findPastOwners(userId, now);
            case FUTURE ->
                    bookingList = bookingRepository.findFutureByOwner(userId, now);
            case CURRENT ->
                    bookingList = bookingRepository.findCurrentOwners(userId, now, now);
            case WAITING ->
                    bookingList = bookingRepository.findBookingsByOwnersAndStatus(userId, BookingStatus.WAITING);
            case REJECTED ->
                    bookingList = bookingRepository.findBookingsByOwnersAndStatus(userId, BookingStatus.REJECTED);
            default ->
                    throw new IllegalArgumentException("Неверный статус: " + state);
        }

        return bookingList.stream()
                .map(BookingMapper::mapToOutBookingDto)
                .collect(Collectors.toList());
    }

}
