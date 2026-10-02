package ru.practicum.shareit;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.client.ItemRequestClient;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemRequestControllerGateway.class)
@ContextConfiguration(classes = ShareItGateway.class)
public class ItemRequestControllerGatewayTest {

    @MockBean
    private ItemRequestClient itemRequestClient;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mvc;
    private ItemRequestDto itemRequestDto;

    @BeforeEach
    void setUp() {
        itemRequestDto = new ItemRequestDto("Нужна дрель");
    }


    @Test
    void findByIdItemRequest() throws Exception {
        when(itemRequestClient.findById(1L, 1L))
                .thenReturn(ResponseEntity.ok().build());

        mvc.perform(get("/requests/1")
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(itemRequestClient, times(1)).findById(1L, 1L);
    }

    @Test
    void createItemRequest() throws Exception {
        when(itemRequestClient.create(any(ItemRequestDto.class), anyLong()))
                .thenReturn(ResponseEntity.ok().build());

        mvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(itemRequestDto)))
                .andExpect(status().isOk());

        verify(itemRequestClient, times(1)).create(any(ItemRequestDto.class), eq(1L));
    }

    @Test
    void getAllItemRequest() throws Exception {
        when(itemRequestClient.getAllRequests(anyLong()))
                .thenReturn(ResponseEntity.ok().build());

        mvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(itemRequestClient, times(1)).getAllRequests(1L);
    }

    @Test
    void getUserItemRequest() throws Exception {
        when(itemRequestClient.getUserRequests(1L))
                .thenReturn(ResponseEntity.ok().build());

        mvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(itemRequestClient, times(1)).getUserRequests(1L);
    }
}
