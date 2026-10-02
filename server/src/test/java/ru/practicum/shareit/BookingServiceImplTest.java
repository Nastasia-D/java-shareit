package ru.practicum.shareit;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingOutDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Collection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

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
