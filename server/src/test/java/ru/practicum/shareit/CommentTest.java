package ru.practicum.shareit;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;


public class CommentTest {

    @Test
    void testCommentGettersAndSetters() {
        LocalDateTime now = LocalDateTime.now();
        Item item = new Item();
        item.setId(1L);
        User author = new User();
        author.setId(2L);

        Comment comment = new Comment();
        comment.setId(10L);
        comment.setText("Отличный товар");
        comment.setItem(item);
        comment.setAuthor(author);
        comment.setCreated(now);

        assertThat(comment.getId()).isEqualTo(10L);
        assertThat(comment.getText()).isEqualTo("Отличный товар");
        assertThat(comment.getItem()).isEqualTo(item);
        assertThat(comment.getAuthor()).isEqualTo(author);
        assertThat(comment.getCreated()).isEqualTo(now);
        assertThat(comment.toString()).contains("Comment");
    }

    @Test
    void testEqualsAndHashCode() {
        Comment comment1 = new Comment();
        comment1.setId(1L);

        Comment comment2 = new Comment();
        comment2.setId(1L);

        Comment comment3 = new Comment();
        comment3.setId(2L);

        Comment commentNullId = new Comment();

        assertThat(comment1).isEqualTo(comment1);
        assertThat(comment1).isEqualTo(comment2);
        assertThat(comment1).isNotEqualTo(comment3);
        assertThat(comment1).isNotEqualTo(null);
        assertThat(comment1).isNotEqualTo("some string");
        assertThat(commentNullId).isNotEqualTo(comment1);

        assertThat(comment1.hashCode()).isEqualTo(comment2.hashCode());
        assertThat(commentNullId.hashCode()).isNotZero();
    }
}
