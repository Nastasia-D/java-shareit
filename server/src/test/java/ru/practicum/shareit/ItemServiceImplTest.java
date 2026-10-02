package ru.practicum.shareit;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemBookingDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

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
public class ItemServiceImplTest {

    private final EntityManager em;
    private final ItemService itemService;
    private final UserService userService;

    @Test
    void createItem() {
        User owner = saveUser("owner@email.com", "Владелец");
        em.persist(owner);
        em.flush();

        ItemDto itemDto = new ItemDto();
        itemDto.setName("Шуруповерт");
        itemDto.setDescription("Аккумуляторный шуруповерт");
        itemDto.setAvailable(true);

        ItemDto result = itemService.create(itemDto, owner.getId());

        assertThat(result, notNullValue());
        assertThat(result.getId(), notNullValue());
        assertThat(result.getName(), equalTo("Шуруповерт"));
    }

    @Test
    void createItem_() {
        ItemDto itemDto = new ItemDto();
        itemDto.setName("Шуруповерт");
        itemDto.setDescription("Аккумуляторный шуруповерт");
        itemDto.setAvailable(true);

        assertThrows(NotFoundException.class, () -> itemService.create(itemDto, 999L));
    }

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

    @Test
    void updateItem_WhenNotOwner() {
        User owner = saveUser("owner@email.com", "Владелец");
        em.persist(owner);
        User anotherUser = saveUser("other@email.com", "Чужой");
        em.persist(anotherUser);

        Item item = saveItem("Молоток", "Обычный молоток", true, owner);
        em.persist(item);
        em.flush();

        ItemDto updateDto = new ItemDto();
        updateDto.setName("Попытка взлома");

        assertThrows(NotFoundException.class, () -> itemService.update(anotherUser.getId(), item.getId(), updateDto));
    }

    @Test
    void addComment_WhenNoBookingns() {
        User owner = saveUser("owner@email.com", "Владелец");
        em.persist(owner);
        User user = saveUser("user@email.com", "Пользователь");
        em.persist(user);

        Item item = saveItem("Молоток", "Обычный молоток", true, owner);
        em.persist(item);
        em.flush();

        CommentDto commentDto = new CommentDto();
        commentDto.setText("Отличная вещь!");

        assertThrows(ValidationException.class, () -> itemService.addComment(user.getId(), item.getId(), commentDto));
    }

    @Test
    void getItemsByOwner() {
        User owner = saveUser("owner@email.com", "Владелец");
        em.persist(owner);

        Item item = saveItem("Молоток", "Обычный молоток", true, owner);
        em.persist(item);
        em.flush();

        Collection<ItemBookingDto> result = itemService.getItemsByOwner(owner.getId());

        assertThat(result, hasSize(1));
        assertThat(result.iterator().next().getName(), equalTo("Молоток"));
    }

    @Test
    void findById_AsOwner() {
        User owner = saveUser("owner@email.com", "Владелец");
        em.persist(owner);

        Item item = saveItem("Молоток", "Обычный молоток", true, owner);
        em.persist(item);
        em.flush();

        ItemBookingDto result = itemService.findById(item.getId(), owner.getId());

        assertThat(result, notNullValue());
        assertThat(result.getName(), equalTo("Молоток"));
    }

    @Test
    void updateItem() {
        User owner = saveUser("owner@email.com", "Владелец");
        em.persist(owner);

        Item item = saveItem("Молоток", "Обычный молоток", true, owner);
        em.persist(item);
        em.flush();

        ItemDto updateDto = new ItemDto();
        updateDto.setName("Молоток измененный");
        updateDto.setAvailable(false);

        ItemDto result = itemService.update(owner.getId(), item.getId(), updateDto);

        assertThat(result, notNullValue());
        assertThat(result.getName(), equalTo("Молоток измененный"));
        assertThat(result.getAvailable(), equalTo(false));
    }

    @Test
    void searchItems_WhenTextIsEmptyOrBlank() {
        Collection<ItemDto> resultNull = itemService.searchItems(null);
        assertThat(resultNull, empty());

        Collection<ItemDto> resultBlank = itemService.searchItems("   ");
        assertThat(resultBlank, empty());
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
