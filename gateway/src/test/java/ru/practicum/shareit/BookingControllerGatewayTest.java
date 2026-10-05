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
import ru.practicum.shareit.client.BookingClient;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
@ContextConfiguration(classes = ShareItGateway.class)
public class BookingControllerGatewayTest {

    @MockBean
    private BookingClient bookingClient;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mvc;
    private BookingDto bookingDto;

    @BeforeEach
    void setUp() {
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(2);

        bookingDto = new BookingDto();
        bookingDto.setItemId(1L);
        bookingDto.setStart(start);
        bookingDto.setEnd(end);
    }

    @Test
    void findByIdBooking() throws Exception {
        when(bookingClient.findById(1L, 1L))
                .thenReturn(ResponseEntity.ok().build());

        mvc.perform(get("/bookings/1")
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(bookingClient, times(1)).findById(anyLong(), anyLong());
    }

    @Test
    void createBooking() throws Exception {
        when(bookingClient.create(any(BookingDto.class), eq(1L)))
                .thenReturn(ResponseEntity.ok().build());

        mvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(bookingDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(bookingClient, times(1)).create(any(BookingDto.class), eq(1L));
    }

    @Test
    void approvedBooking() throws Exception {
        when(bookingClient.approved(eq(1L), eq(1L), eq(true)))
                .thenReturn(ResponseEntity.ok().build());

        mvc.perform(patch("/bookings/1")
                        .header("X-Sharer-User-Id", 1L)
                        .param("approved", "true")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(bookingClient, times(1)).approved(eq(1L), eq(1L), eq(true));
    }

    @Test
    void getUserBookings() throws Exception {
        when(bookingClient.getUserBookings(eq(1L), any(BookingState.class)))
                .thenReturn(ResponseEntity.ok().build());

        mvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .param("state", "ALL")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(bookingClient, times(1)).getUserBookings(eq(1L), any(BookingState.class));
    }

    @Test
    void getOwnerBookings() throws Exception {
        when(bookingClient.getOwnerBookings(eq(1L), any(BookingState.class)))
                .thenReturn(ResponseEntity.ok().build());

        mvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", 1L)
                        .param("state", "ALL")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(bookingClient, times(1)).getOwnerBookings(eq(1L), any(BookingState.class));
    }
}
