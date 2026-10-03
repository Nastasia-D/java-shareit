package ru.practicum.shareit;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class ItemRequestTest {

    @Test
    void testItemRequestGettersAndSetters() {
        LocalDateTime now = LocalDateTime.now();
        User requestor = new User();
        requestor.setId(1L);

        ItemRequest request = new ItemRequest();
        request.setId(10L);
        request.setDescription("Хотел бы арендовать шуруповерт");
        request.setCreated(now);
        request.setRequestor(requestor);

        assertThat(request.getId()).isEqualTo(10L);
        assertThat(request.getDescription()).isEqualTo("Хотел бы арендовать шуруповерт");
        assertThat(request.getCreated()).isEqualTo(now);
        assertThat(request.getRequestor()).isEqualTo(requestor);
        assertThat(request.toString()).contains("ItemRequest");
    }

    @Test
    void testEqualsAndHashCode() {
        ItemRequest request1 = new ItemRequest();
        request1.setId(1L);

        ItemRequest request2 = new ItemRequest();
        request2.setId(1L);

        ItemRequest request3 = new ItemRequest();
        request3.setId(2L);

        ItemRequest requestNullId1 = new ItemRequest();
        ItemRequest requestNullId2 = new ItemRequest();

        assertThat(request1).isEqualTo(request1);
        assertThat(request1).isEqualTo(request2);
        assertThat(request1).isNotEqualTo(request3);
        assertThat(request1).isNotEqualTo(null);
        assertThat(request1).isNotEqualTo("some string");
        assertThat(requestNullId1).isNotEqualTo(request2);
        assertThat(requestNullId1).isNotEqualTo(requestNullId2);

        assertThat(request1.hashCode()).isEqualTo(request2.hashCode());
        assertThat(requestNullId1.hashCode()).isEqualTo(requestNullId2.hashCode());
    }
}
