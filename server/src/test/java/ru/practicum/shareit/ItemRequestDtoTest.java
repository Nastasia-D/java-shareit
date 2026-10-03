package ru.practicum.shareit;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.ItemShortDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestOutDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ItemRequestDtoTest {
    @Test
    void testItemRequestDto() {

        ItemRequestDto dto = new ItemRequestDto("Нужна дрель для ремонта");
        assertThat(dto.getDescription()).isEqualTo("Нужна дрель для ремонта");

        ItemRequestDto emptyDto = new ItemRequestDto();
        emptyDto.setDescription("Ищу шуруповерт");
        assertThat(emptyDto.getDescription()).isEqualTo("Ищу шуруповерт");
    }

    @Test
    void testItemRequestOutDto() {
        LocalDateTime now = LocalDateTime.now();
        ItemShortDto item = new ItemShortDto();
        item.setId(1L);
        item.setName("Дрель");
        List<ItemShortDto> items = List.of(item);

        ItemRequestOutDto dto = new ItemRequestOutDto(1L, "Нужна дрель", now, items);

        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getDescription()).isEqualTo("Нужна дрель");
        assertThat(dto.getCreated()).isEqualTo(now);
        assertThat(dto.getItems()).isEqualTo(items);

        ItemRequestOutDto emptyDto = new ItemRequestOutDto();
        emptyDto.setId(2L);
        emptyDto.setDescription("Нужен молоток");
        emptyDto.setCreated(now);
        emptyDto.setItems(items);

        assertThat(emptyDto.getId()).isEqualTo(2L);
        assertThat(emptyDto.getDescription()).isEqualTo("Нужен молоток");
        assertThat(emptyDto.getCreated()).isEqualTo(now);
        assertThat(emptyDto.getItems()).hasSize(1);
    }
}
