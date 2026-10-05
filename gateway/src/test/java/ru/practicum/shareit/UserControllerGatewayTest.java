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
import ru.practicum.shareit.client.UserClient;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
@ContextConfiguration(classes = ShareItGateway.class)
public class UserControllerGatewayTest {

    @MockBean
    private UserClient userClient;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mvc;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        userDto = new UserDto(
                1L,
                "Пользователь_1",
                "Email@email.com");
    }

    @Test
    void createUser() throws Exception {
        when(userClient.create(any()))
                .thenReturn(ResponseEntity.ok().build());

        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(userClient, times(1)).create(any(UserDto.class));
    }

    @Test
    void updateUser() throws Exception {
        UserDto updateDto = new UserDto(1L, "Обновленное Имя", "update@email.com");

        when(userClient.update(eq(1L), any(UserDto.class)))
                .thenReturn(ResponseEntity.ok().build());

        mvc.perform(patch("/users/1")
                        .content(mapper.writeValueAsString(updateDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(userClient, times(1)).update(eq(1L), any(UserDto.class));
    }

    @Test
    void deleteUser() throws Exception {
        when(userClient.delete(1L))
                .thenReturn(ResponseEntity.ok().build());

        mvc.perform(delete("/users/1"))
                .andExpect(status().isOk());

        verify(userClient, times(1)).delete(1L);
    }

    @Test
    void findAllUsers() throws Exception {
        List<UserDto> users = List.of(
                userDto,
                new UserDto(2L, "Пользователь_2", "user2@email.com")
        );

        when(userClient.findAll())
                .thenReturn(ResponseEntity.ok().build());

        mvc.perform(get("/users")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(userClient, times(1)).findAll();
    }

    @Test
    void findByIdUser() throws Exception {
        when(userClient.findById(1L))
                .thenReturn(ResponseEntity.ok().build());

        mvc.perform(get("/users/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(userClient, times(1)).findById(eq(1L));
    }

}
