package ru.practicum.shareit;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.model.User;

import static org.assertj.core.api.Assertions.assertThat;

public class UserTest {

    @Test
    void testUserGettersAndSetters() {
        User user = new User();
        user.setId(1L);
        user.setName("Иван");
        user.setEmail("ivan@email.com");

        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getName()).isEqualTo("Иван");
        assertThat(user.getEmail()).isEqualTo("ivan@email.com");
        assertThat(user.toString()).contains("User");
    }

    @Test
    void testEqualsAndHashCode() {
        User user1 = new User();
        user1.setId(1L);

        User user2 = new User();
        user2.setId(1L);

        User user3 = new User();
        user3.setId(2L);

        User userNullId1 = new User();
        User userNullId2 = new User();

        assertThat(user1).isEqualTo(user1);
        assertThat(user1).isEqualTo(user2);
        assertThat(user1).isNotEqualTo(user3);
        assertThat(user1).isNotEqualTo(null);
        assertThat(user1).isNotEqualTo("some string");
        assertThat(userNullId1).isNotEqualTo(user2);
        assertThat(userNullId1).isNotEqualTo(userNullId2);

        assertThat(user1.hashCode()).isEqualTo(user2.hashCode());
        assertThat(userNullId1.hashCode()).isEqualTo(userNullId2.hashCode());
    }
}
