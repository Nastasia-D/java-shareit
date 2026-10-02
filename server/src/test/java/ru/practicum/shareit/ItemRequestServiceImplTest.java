package ru.practicum.shareit;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestService;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestOutDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

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
public class ItemRequestServiceImplTest {

    private final EntityManager em;
    private final ItemRequestService itemRequestService;

    @Test
    void createItemRequestTest() {
        User requester = saveUser("user@email.com", "Арендатор");
        em.persist(requester);
        em.flush();

        ItemRequestDto requestDto = saveItemRequestDto("Нужна дрель для ремонта");

        ItemRequestOutDto createdRequest = itemRequestService.create(requestDto, requester.getId());

        TypedQuery<ItemRequest> query = em.createQuery(
                "SELECT r FROM ItemRequest r WHERE r.id = :id", ItemRequest.class);
        ItemRequest savedRequest = query.setParameter("id", createdRequest.getId()).getSingleResult();

        assertThat(savedRequest.getId(), notNullValue());
        assertThat(savedRequest.getDescription(), equalTo(requestDto.getDescription()));
        assertThat(savedRequest.getRequestor().getId(), equalTo(requester.getId()));
        assertThat(savedRequest.getCreated(), notNullValue());

    }

    @Test
    void createItemRequest_WhenUserNotFound() {
        ItemRequestDto requestDto = saveItemRequestDto("Нужна дрель");
        assertThrows(NotFoundException.class, () -> itemRequestService.create(requestDto, 999L));
    }

    private User saveUser(String name, String email) {
        User user = new User();
        user.setEmail(email);
        user.setName(name);
        return user;
    }

    @Test
    void findByIdTest() {
        User requester = saveUser("user@email.com", "Пользователь");
        em.persist(requester);

        ItemRequest request = saveItemRequest("Нужен шуруповерт", requester, LocalDateTime.now());
        em.persist(request);
        em.flush();

        ItemRequestOutDto found = itemRequestService.findById(request.getId(), requester.getId());

        assertThat(found, notNullValue());
        assertThat(found.getId(), equalTo(request.getId()));
        assertThat(found.getDescription(), equalTo("Нужен шуруповерт"));
    }

    @Test
    void findById_WhenUserNotFound() {
        assertThrows(NotFoundException.class, () -> itemRequestService.findById(1L, 999L));
    }

    @Test
    void findById_WhenRequestNotFound() {
        User user = saveUser("user@email.com", "Пользователь");
        em.persist(user);
        em.flush();

        assertThrows(NotFoundException.class, () -> itemRequestService.findById(999L, user.getId()));
    }

    @Test
    void getUserRequestsTest() {
        User requester = saveUser("user@email.com", "Пользователь");
        em.persist(requester);

        ItemRequest request = saveItemRequest("Нужна палатка", requester, LocalDateTime.now());
        em.persist(request);

        User owner = saveUser("owner@email.com", "Владелец");
        em.persist(owner);

        Item item = saveItem("Палатка 3-местная", "Большая", true, owner, request.getId());
        em.persist(item);
        em.flush();

        List<ItemRequestOutDto> requests = itemRequestService.getUserRequests(requester.getId());

        assertThat(requests, hasSize(1));
        assertThat(requests.get(0).getItems(), hasSize(1));
        assertThat(requests.get(0).getItems().get(0).getName(), equalTo("Палатка 3-местная"));
    }

    @Test
    void getUserRequests_WhenUserNotFound() {
        assertThrows(NotFoundException.class, () -> itemRequestService.getUserRequests(999L));
    }

    @Test
    void getAllRequestsTest() {
        User requester = saveUser("user@email.com", "Пользователь");
        em.persist(requester);

        User otherUser = saveUser("other@email.com", "Другой");
        em.persist(otherUser);

        ItemRequest request = saveItemRequest("Нужен велосипед", otherUser, LocalDateTime.now());
        em.persist(request);
        em.flush();

        List<ItemRequestOutDto> requests = itemRequestService.getAllRequests(requester.getId());

        assertThat(requests, hasSize(1));
        assertThat(requests.get(0).getDescription(), equalTo("Нужен велосипед"));
    }

    private ItemRequest saveItemRequest(String description, User requestor, LocalDateTime created) {
        ItemRequest request = new ItemRequest();
        request.setDescription(description);
        request.setRequestor(requestor);
        request.setCreated(created);
        return request;
    }

    private ItemRequestDto saveItemRequestDto(String description) {
        ItemRequestDto dto = new ItemRequestDto();
        dto.setDescription(description);
        return dto;
    }

    private Item saveItem(String name, String description, Boolean available, User owner, Long requestId) {
        Item item = new Item();
        item.setName(name);
        item.setDescription(description);
        item.setAvailable(available);
        item.setOwner(owner);
        item.setRequestId(requestId);
        return item;
    }
}
