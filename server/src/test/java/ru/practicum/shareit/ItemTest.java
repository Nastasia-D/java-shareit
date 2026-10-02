package ru.practicum.shareit;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import static org.assertj.core.api.Assertions.assertThat;

public class ItemTest {

    @Test
    void testItemGettersAndSetters() {
        User owner = new User();
        owner.setId(1L);

        Item item = new Item();
        item.setId(10L);
        item.setName("Дрель");
        item.setDescription("Мощная аккумуляторная дрель");
        item.setAvailable(true);
        item.setOwner(owner);
        item.setRequestId(5L);

        assertThat(item.getId()).isEqualTo(10L);
        assertThat(item.getName()).isEqualTo("Дрель");
        assertThat(item.getDescription()).isEqualTo("Мощная аккумуляторная дрель");
        assertThat(item.getAvailable()).isTrue();
        assertThat(item.getOwner()).isEqualTo(owner);
        assertThat(item.getRequestId()).isEqualTo(5L);
        assertThat(item.toString()).contains("Item");
    }

    @Test
    void testEqualsAndHashCode() {
        Item item1 = new Item();
        item1.setId(1L);

        Item item2 = new Item();
        item2.setId(1L);

        Item item3 = new Item();
        item3.setId(2L);

        Item itemNullId1 = new Item();
        Item itemNullId2 = new Item();

        assertThat(item1).isEqualTo(item1);
        assertThat(item1).isEqualTo(item2);
        assertThat(item1).isNotEqualTo(item3);
        assertThat(item1).isNotEqualTo(null);
        assertThat(item1).isNotEqualTo("some string");
        assertThat(itemNullId1).isNotEqualTo(item2);
        assertThat(itemNullId1).isNotEqualTo(itemNullId2);

        assertThat(item1.hashCode()).isEqualTo(item2.hashCode());
        assertThat(itemNullId1.hashCode()).isEqualTo(itemNullId2.hashCode());
    }
}
