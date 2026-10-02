package ru.practicum.shareit;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

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
public class ItemServiceImplTest {

    private final EntityManager em;
    private final ItemService itemService;
    private final UserService userService;

    @Test
    void searchItemsTest() {
        User owner = saveUser("user@email.com", "Владелец");
        em.persist(owner);

        Item availableItem = saveItem("Дрель Аккумуляторная", "Мощный инструмент", true, owner);
        em.persist(availableItem);

        Item unavailableItem = saveItem("Дрель Ручная", "Старая дрель", false, owner);
        em.persist(unavailableItem);

        em.flush();

        Collection<ItemDto> result = itemService.searchItems("дрель");

        assertThat(result, hasSize(1));
        assertThat(result.iterator().next().getName(), equalTo("Дрель Аккумуляторная"));
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
}
