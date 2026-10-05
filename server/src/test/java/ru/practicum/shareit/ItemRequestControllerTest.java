package ru.practicum.shareit;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.ItemRequestController;
import ru.practicum.shareit.request.ItemRequestService;
import ru.practicum.shareit.request.dto.ItemRequestOutDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemRequestController.class)
@ContextConfiguration(classes = ShareItServer.class)
public class ItemRequestControllerTest {

    @MockBean
    private ItemRequestService itemRequestService;
    @InjectMocks
    private ItemRequestController itemRequestController;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mvc;
    private ItemRequestDto itemRequestDto;
    private ItemRequestOutDto itemRequestOutDto;

    @BeforeEach
    void setUp() {
        itemRequestDto = new ItemRequestDto();
        itemRequestDto.setDescription("Нужна дрель");

        itemRequestOutDto = new ItemRequestOutDto();
        itemRequestOutDto.setId(1L);
        itemRequestOutDto.setDescription("Нужна дрель");
        itemRequestOutDto.setCreated(LocalDateTime.now());
        itemRequestOutDto.setItems(List.of());
    }

    @Test
    void findByIdItemRequest() throws Exception {
        when(itemRequestService.findById(1L, 1L))
                .thenReturn(itemRequestOutDto);

        mvc.perform(get("/requests/1")
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemRequestOutDto.getId()))
                .andExpect(jsonPath("$.description").value(itemRequestOutDto.getDescription()));
    }

    @Test
    void createItemRequest() throws Exception {
        when(itemRequestService.create(any(ItemRequestDto.class), eq(1L)))
                .thenReturn(itemRequestOutDto);

        mvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(itemRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemRequestOutDto.getId()))
                .andExpect(jsonPath("$.description").value(itemRequestOutDto.getDescription()));
    }

    @Test
    void getAllItemRequest() throws Exception {
        when(itemRequestService.getAllRequests(1L))
                .thenReturn(List.of(itemRequestOutDto));

        mvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(itemRequestOutDto.getId()))
                .andExpect(jsonPath("$[0].description").value(itemRequestOutDto.getDescription()));
    }

    @Test
    void getUserItemRequest() throws Exception {
        when(itemRequestService.getUserRequests(1L))
                .thenReturn(List.of(itemRequestOutDto));

        mvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(itemRequestOutDto.getId()))
                .andExpect(jsonPath("$[0].description").value(itemRequestOutDto.getDescription()));
    }
}
