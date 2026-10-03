package ru.practicum.shareit;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.dto.CommentOutDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class CommentDtoTest {

    @Test
    void testCommentDto() {
        CommentDto dto = new CommentDto("Отличный инструмент!");
        assertThat(dto.getText()).isEqualTo("Отличный инструмент!");

        CommentDto emptyDto = new CommentDto();
        emptyDto.setText("Все прошло отлично.");
        assertThat(emptyDto.getText()).isEqualTo("Все прошло отлично.");
    }

    @Test
    void testCommentOutDto() {
        LocalDateTime now = LocalDateTime.now();

        CommentOutDto dto = new CommentOutDto(1L, "Понравилось", "Иван", now);

        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getText()).isEqualTo("Понравилось");
        assertThat(dto.getAuthorName()).isEqualTo("Иван");
        assertThat(dto.getCreated()).isEqualTo(now);

        CommentOutDto emptyDto = new CommentOutDto();
        emptyDto.setId(2L);
        emptyDto.setText("Супер");
        emptyDto.setAuthorName("Алексей");
        emptyDto.setCreated(now);

        assertThat(emptyDto.getId()).isEqualTo(2L);
        assertThat(emptyDto.getText()).isEqualTo("Супер");
        assertThat(emptyDto.getAuthorName()).isEqualTo("Алексей");
        assertThat(emptyDto.getCreated()).isEqualTo(now);
    }
}
