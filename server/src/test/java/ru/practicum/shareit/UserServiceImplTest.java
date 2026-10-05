package ru.practicum.shareit;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collection;
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
public class UserServiceImplTest {

    private final EntityManager em;

    private final UserService userService;

    @Test
    void createUserTest() {
        UserDto userDto = new UserDto();
        userDto.setName("Иван");
        userDto.setEmail("ivan@email.com");

        UserDto createdUser = userService.create(userDto);

        assertThat(createdUser, notNullValue());
        assertThat(createdUser.getId(), notNullValue());
        assertThat(createdUser.getName(), equalTo("Иван"));
        assertThat(createdUser.getEmail(), equalTo("ivan@email.com"));
    }

    @Test
    void findByIdTest() {
        User user = saveUser("Алексей", "alex@email.com");
        em.persist(user);
        em.flush();

        UserDto foundUser = userService.findById(user.getId());

        assertThat(foundUser, notNullValue());
        assertThat(foundUser.getId(), equalTo(user.getId()));
        assertThat(foundUser.getName(), equalTo("Алексей"));
    }

    @Test
    void findAllTest() {
        List<User> sourceUsers = List.of(
                saveUser("Пользователь_1", "petr@email.com"),
                saveUser("Пользователь_2", "vasilii@email.com")
        );

        for (User user : sourceUsers) {
            em.persist(user);
        }
        em.flush();

        Collection<UserDto> targetUsers = userService.findAll();

        assertThat(targetUsers, hasSize(sourceUsers.size()));
        for (User sourceUser : sourceUsers) {
            assertThat(targetUsers, hasItem(allOf(
                    hasProperty("id", notNullValue()),
                    hasProperty("name", equalTo(sourceUser.getName())),
                    hasProperty("email", equalTo(sourceUser.getEmail()))
            )));
        }
    }

    @Test
    void updateTest() {
        User user = saveUser("СтароеИмя", "old@email.com");
        em.persist(user);
        em.flush();

        UserDto updateDto = new UserDto();
        updateDto.setName("НовоеИмя");
        updateDto.setEmail("new@email.com");

        UserDto updatedUser = userService.update(user.getId(), updateDto);

        assertThat(updatedUser, notNullValue());
        assertThat(updatedUser.getName(), equalTo("НовоеИмя"));
        assertThat(updatedUser.getEmail(), equalTo("new@email.com"));
    }

    @Test
    void deleteTest() {
        User user = saveUser("Удаляемый", "delete@email.com");
        em.persist(user);
        em.flush();

        userService.delete(user.getId());

        User found = em.find(User.class, user.getId());
        assertThat(found, nullValue());
    }

    @Test
    void update_WhenUserNotFound() {
        UserDto updateDto = new UserDto();
        updateDto.setName("Имя");

        assertThrows(NotFoundException.class, () -> userService.update(999L, updateDto));
    }

    private User saveUser(String name, String email) {
        User user = new User();
        user.setEmail(email);
        user.setName(name);
        return user;
    }
}
