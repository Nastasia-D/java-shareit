package ru.practicum.shareit;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.dto.CommentOutDto;
import ru.practicum.shareit.item.dto.ItemBookingDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
@ContextConfiguration(classes = ShareItServer.class)
public class ItemControllerTest {

    @MockBean
    private ItemService itemService;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mvc;
    private ItemDto itemDto;
    private ItemBookingDto itemBookingDto;
    private CommentDto commentDto;
    private CommentOutDto commentOutDto;


    @BeforeEach
    void setUp() {
        itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName("Дрель");
        itemDto.setDescription("Аккумуляторная");
        itemDto.setAvailable(true);

        itemBookingDto = new ItemBookingDto();
        itemBookingDto.setId(1L);
        itemBookingDto.setName("Дрель");
        itemBookingDto.setDescription("Аккумуляторная");
        itemBookingDto.setAvailable(true);
        itemBookingDto.setComments(List.of());

        commentDto = new CommentDto();
        commentDto.setText("Супер дрель!");

        commentOutDto = new CommentOutDto();
        commentOutDto.setId(1L);
        commentOutDto.setText("Супер дрель!");
        commentOutDto.setAuthorName("Арендодатель");
        commentOutDto.setCreated(LocalDateTime.now());
    }

    @Test
    void findByIdItem() throws Exception {
        when(itemService.findById(1L, 1L))
                .thenReturn(itemBookingDto);

        mvc.perform(get("/items/1")
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemBookingDto.getId()))
                .andExpect(jsonPath("$.name").value(itemBookingDto.getName()));
    }

    @Test
    void createItem() throws Exception {
        when(itemService.create(any(ItemDto.class), eq(1L)))
                .thenReturn(itemDto);

        mvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemDto.getId()))
                .andExpect(jsonPath("$.name").value(itemDto.getName()))
                .andExpect(jsonPath("$.description").value(itemDto.getDescription()))
                .andExpect(jsonPath("$.available").value(itemDto.getAvailable()));
    }

    @Test
    void searchItemsItem() throws Exception {
        when(itemService.searchItems("дрель"))
                .thenReturn(List.of(itemDto));

        mvc.perform(get("/items/search")
                        .param("text", "дрель")
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(itemDto.getId()))
                .andExpect(jsonPath("$[0].name").value(itemDto.getName()));
    }

    @Test
    void updateItem() throws Exception {
        when(itemService.update(eq(1L), eq(1L), any(ItemDto.class)))
                .thenReturn(itemDto);

        mvc.perform(patch("/items/1")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemDto.getId()))
                .andExpect(jsonPath("$.name").value(itemDto.getName()));
    }

    @Test
    void addCommentItem() throws Exception {
        when(itemService.addComment(eq(1L), eq(1L), any(CommentDto.class)))
                .thenReturn(commentOutDto);

        mvc.perform(post("/items/1/comment")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(commentDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(commentOutDto.getId()))
                .andExpect(jsonPath("$.text").value(commentOutDto.getText()))
                .andExpect(jsonPath("$.authorName").value(commentOutDto.getAuthorName()));
    }

    @Test
    void getItemsByOwnerItem() throws Exception {
        when(itemService.getItemsByOwner(1L))
                .thenReturn(List.of(itemBookingDto));

        mvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(itemBookingDto.getId()))
                .andExpect(jsonPath("$[0].name").value(itemBookingDto.getName()));
    }
}
