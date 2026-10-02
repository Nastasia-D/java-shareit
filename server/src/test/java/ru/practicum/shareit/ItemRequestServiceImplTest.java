package ru.practicum.shareit;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestService;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestOutDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

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

    private User saveUser(String name, String email) {
        User user = new User();
        user.setEmail(email);
        user.setName(name);
        return user;
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
