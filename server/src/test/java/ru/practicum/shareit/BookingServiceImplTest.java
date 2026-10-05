package ru.practicum.shareit;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingOutDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Collection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest(
        classes = {ShareItServer.class},
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE
)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingServiceImplTest {

    private final EntityManager em;
    private final BookingService bookingService;

    @Test
    void creteBookingsTest() {
        User owner = saveUser("owner@email.com", "Владелец");
        em.persist(owner);
        User booker = saveUser("booker@email.com", "Арендатор");
        em.persist(booker);
        Item item = saveItem("Перфоратор", "Мощный 900Вт", true, owner);
        em.persist(item);
        LocalDateTime now = LocalDateTime.now();
        BookingDto dto = new BookingDto();
        dto.setItemId(item.getId());
        dto.setStart(now.plusDays(1));
        dto.setEnd(now.plusDays(2));

        BookingOutDto result = bookingService.create(dto, booker.getId());

        assertThat(result, notNullValue());
        assertThat(result.getStatus(), equalTo(BookingStatus.WAITING));
        assertThat(result.getItem().getName(), equalTo("Перфоратор"));
        assertThat(result.getBooker().getId(), equalTo(booker.getId()));
    }

    @Test
    void creteBookingsTestValidation__WhenItemNotAvailable() {
        User owner = saveUser("owner@email.com", "Владелец");
        em.persist(owner);
        User booker = saveUser("booker@email.com", "Арендатор");
        em.persist(booker);
        Item item = saveItem("Перфоратор", "Мощный 900Вт", false, owner);
        em.persist(item);
        LocalDateTime now = LocalDateTime.now();
        BookingDto dto = new BookingDto();
        dto.setItemId(item.getId());
        dto.setStart(now.plusDays(1));
        dto.setEnd(now.plusDays(2));

        assertThrows(ValidationException.class, () -> bookingService.create(dto, booker.getId()));
    }

    @Test
    void creteBookingsTestValidation_WhenOwnerNotAvailable() {
        User owner = saveUser("owner@email.com", "Владелец");
        em.persist(owner);
        Item item = saveItem("Перфоратор", "Мощный 900Вт", true, owner);
        em.persist(item);
        LocalDateTime now = LocalDateTime.now();
        BookingDto dto = new BookingDto();
        dto.setItemId(item.getId());
        dto.setStart(now.plusDays(1));
        dto.setEnd(now.plusDays(2));

        assertThrows(NotFoundException.class, () -> bookingService.create(dto, owner.getId()));
    }

    @Test
    void createBooking_WhenEndBeforeStart() {
        User owner = saveUser("owner@email.com", "Владелец");
        em.persist(owner);
        User booker = saveUser("booker@email.com", "Арендатор");
        em.persist(booker);
        Item item = saveItem("Перфоратор", "Мощный 900Вт", true, owner);
        em.persist(item);
        em.flush();

        LocalDateTime now = LocalDateTime.now();
        BookingDto dto = new BookingDto();
        dto.setItemId(item.getId());
        dto.setStart(now.plusDays(2));
        dto.setEnd(now.plusDays(1)); // Конец раньше начала

        assertThrows(ValidationException.class, () -> bookingService.create(dto, booker.getId()));
    }

    @Test
    void approvedBooking() {
        User owner = saveUser("owner@email.com", "Владелец");
        em.persist(owner);
        User booker = saveUser("booker@email.com", "Арендатор");
        em.persist(booker);
        Item item = saveItem("Перфоратор", "Мощный 900Вт", true, owner);
        em.persist(item);

        LocalDateTime now = LocalDateTime.now();
        Booking booking = saveBooking(now.plusDays(1), now.plusDays(2), item, booker, BookingStatus.WAITING);
        em.persist(booking);
        em.flush();

        BookingOutDto result = bookingService.approved(booking.getId(), owner.getId(), true);

        assertThat(result, notNullValue());
        assertThat(result.getStatus(), equalTo(BookingStatus.APPROVED));
    }

    @Test
    void approvedBooking_AsRejected() {
        User owner = saveUser("owner@email.com", "Владелец");
        em.persist(owner);
        User booker = saveUser("booker@email.com", "Арендатор");
        em.persist(booker);
        Item item = saveItem("Перфоратор", "Мощный 900Вт", true, owner);
        em.persist(item);

        LocalDateTime now = LocalDateTime.now();
        Booking booking = saveBooking(now.plusDays(1), now.plusDays(2), item, booker, BookingStatus.WAITING);
        em.persist(booking);
        em.flush();

        BookingOutDto result = bookingService.approved(booking.getId(), owner.getId(), false);

        assertThat(result, notNullValue());
        assertThat(result.getStatus(), equalTo(BookingStatus.REJECTED));
    }

    @Test
    void approvedBooking_WhenNotOwner() {
        User owner = saveUser("owner@email.com", "Владелец");
        em.persist(owner);
        User booker = saveUser("booker@email.com", "Арендатор");
        em.persist(booker);
        User anotherUser = saveUser("other@email.com", "Чужой");
        em.persist(anotherUser);
        Item item = saveItem("Перфоратор", "Мощный 900Вт", true, owner);
        em.persist(item);

        LocalDateTime now = LocalDateTime.now();
        Booking booking = saveBooking(now.plusDays(1), now.plusDays(2), item, booker, BookingStatus.WAITING);
        em.persist(booking);
        em.flush();

        BookingOutDto result = bookingService.approved(booking.getId(), owner.getId(), true);

        assertThrows(ForbiddenException.class, () -> bookingService.approved(booking.getId(), anotherUser.getId(), true));
    }

    @Test
    void approvedBooking_WhenStatusNotWaiting() {
        User owner = saveUser("owner@email.com", "Владелец");
        em.persist(owner);
        User booker = saveUser("booker@email.com", "Арендатор");
        em.persist(booker);
        Item item = saveItem("Перфоратор", "Мощный 900Вт", true, owner);
        em.persist(item);

        LocalDateTime now = LocalDateTime.now();
        Booking booking = saveBooking(now.plusDays(1), now.plusDays(2), item, booker, BookingStatus.APPROVED);
        em.persist(booking);
        em.flush();

        assertThrows(ValidationException.class, () -> bookingService.approved(booking.getId(), owner.getId(), true));;
    }

    @Test
    void getUserBookingsTest() {
        User owner = saveUser("owner@email.com", "Владелец");
        em.persist(owner);

        User booker = saveUser("booker@email.com", "Арендатор");
        em.persist(booker);

        Item item = saveItem("Перфоратор", "Мощный 900Вт", true, owner);
        em.persist(item);

        LocalDateTime now = LocalDateTime.now();
        Booking futureBooking = saveBooking(now.plusDays(1), now.plusDays(2), item, booker, BookingStatus.WAITING);
        em.persist(futureBooking);

        Booking pastBooking = saveBooking(now.minusDays(5), now.minusDays(4), item, booker, BookingStatus.REJECTED);
        em.persist(pastBooking);

        em.flush();
        Collection<BookingOutDto> allBookings = bookingService.getUserBookings(booker.getId(), BookingState.ALL);
        assertThat(allBookings, hasSize(2));

        Collection<BookingOutDto> waitingBookings = bookingService.getUserBookings(booker.getId(), BookingState.WAITING);
        assertThat(waitingBookings, hasSize(1));
        assertThat(waitingBookings.iterator().next().getId(), equalTo(futureBooking.getId()));

        Collection<BookingOutDto> rejectedBookings = bookingService.getUserBookings(booker.getId(), BookingState.REJECTED);
        assertThat(rejectedBookings, hasSize(1));
        assertThat(rejectedBookings.iterator().next().getId(), equalTo(pastBooking.getId()));

    }

    @Test
    void getUserBookingsTest_WithOtherStates() {
        User owner = saveUser("owner@email.com", "Владелец");
        em.persist(owner);

        User booker = saveUser("booker@email.com", "Арендатор");
        em.persist(booker);

        Item item = saveItem("Перфоратор", "Мощный 900Вт", true, owner);
        em.persist(item);

        LocalDateTime now = LocalDateTime.now();
        Booking currentBooking = saveBooking(now.minusDays(1), now.plusDays(1), item, booker, BookingStatus.APPROVED);
        em.persist(currentBooking);

        Booking pastBooking = saveBooking(now.minusDays(5), now.minusDays(2), item, booker, BookingStatus.APPROVED);
        em.persist(pastBooking);

        Booking futureBooking = saveBooking(now.plusDays(2), now.plusDays(4), item, booker, BookingStatus.APPROVED);
        em.persist(futureBooking);

        em.flush();

        Collection<BookingOutDto> currentBookings = bookingService.getUserBookings(booker.getId(), BookingState.CURRENT);
        assertThat(currentBookings, hasSize(1));
        assertThat(currentBookings.iterator().next().getId(), equalTo(currentBooking.getId()));

        Collection<BookingOutDto> pastBookings = bookingService.getUserBookings(booker.getId(), BookingState.PAST);
        assertThat(pastBookings, hasSize(1));
        assertThat(pastBookings.iterator().next().getId(), equalTo(pastBooking.getId()));

        Collection<BookingOutDto> futureBookings = bookingService.getUserBookings(booker.getId(), BookingState.FUTURE);
        assertThat(futureBookings, hasSize(1));
        assertThat(futureBookings.iterator().next().getId(), equalTo(futureBooking.getId()));

        assertThrows(NotFoundException.class, () -> bookingService.getUserBookings(999L, BookingState.ALL));
    }

    private User saveUser(String name, String email) {
        User user = new User();
        user.setEmail(email);
        user.setName(name);
        return user;
    }

    private Item saveItem(String name, String description, Boolean available, User owner) {
        Item item = new Item();
        item.setName(name);
        item.setDescription(description);
        item.setAvailable(available);
        item.setOwner(owner);
        return item;
    }

    private Booking saveBooking(LocalDateTime start, LocalDateTime end, Item item, User booker, BookingStatus status) {
        Booking booking = new Booking();
        booking.setStart(start);
        booking.setEnd(end);
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setStatus(status);
        return booking;
    }

}
