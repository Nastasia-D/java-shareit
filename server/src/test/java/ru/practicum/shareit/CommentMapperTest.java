package ru.practicum.shareit;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.comment.CommentMapper;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.dto.CommentOutDto;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class CommentMapperTest {

    @Test
    void mapToOutCommentDto_shouldMapSuccessfully() {
        User author = new User();
        author.setName("Иван");

        Comment comment = new Comment();
        comment.setId(1L);
        comment.setText("Отличный товар!");
        comment.setAuthor(author);
        comment.setCreated(LocalDateTime.now());

        CommentOutDto dto = CommentMapper.mapToOutCommentDto(comment);

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getText()).isEqualTo("Отличный товар!");
        assertThat(dto.getAuthorName()).isEqualTo("Иван");
        assertThat(dto.getCreated()).isEqualTo(comment.getCreated());
    }

    @Test
    void mapToOutCommentDto_whenCommentIsNull_shouldReturnNull() {
        CommentOutDto dto = CommentMapper.mapToOutCommentDto(null);
        assertThat(dto).isNull();
    }

    @Test
    void mapToComment_shouldMapSuccessfully() {
        CommentDto dto = new CommentDto();
        dto.setText("Норм");

        Item item = new Item();
        item.setId(5L);

        User author = new User();
        author.setId(2L);

        Comment comment = CommentMapper.mapToComment(dto, item, author);

        assertThat(comment).isNotNull();
        assertThat(comment.getText()).isEqualTo("Норм");
        assertThat(comment.getItem()).isEqualTo(item);
        assertThat(comment.getAuthor()).isEqualTo(author);
    }

    @Test
    void mapToComment_whenDtoIsNull_shouldReturnNull() {
        Comment comment = CommentMapper.mapToComment(null, new Item(), new User());
        assertThat(comment).isNull();
    }
}
