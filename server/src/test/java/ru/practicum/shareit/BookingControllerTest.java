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
import ru.practicum.shareit.booking.BookingController;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingOutDto;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
@ContextConfiguration(classes = ShareItServer.class)
public class BookingControllerTest {

    @MockBean
    private BookingService bookingService;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mvc;
    private BookingDto bookingDto;
    private BookingOutDto bookingOutDto;

    @BeforeEach
    void setUp() {
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(2);

        bookingDto = new BookingDto();
        bookingDto.setItemId(1L);
        bookingDto.setStart(start);
        bookingDto.setEnd(end);

        bookingOutDto = new BookingOutDto();
        bookingOutDto.setId(1L);
        bookingOutDto.setStart(start);
        bookingOutDto.setEnd(end);
        bookingOutDto.setStatus(BookingStatus.WAITING);
    }

    @Test
    void findByIdBooking() throws Exception {
        when(bookingService.findById(1L, 1L))
                .thenReturn(bookingOutDto);

        mvc.perform(get("/bookings/1")
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookingOutDto.getId()))
                .andExpect(jsonPath("$.status").value(bookingOutDto.getStatus().toString()));
    }

    @Test
    void createBooking() throws Exception {
        when(bookingService.create(any(BookingDto.class), eq(1L)))
                .thenReturn(bookingOutDto);

        mvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(bookingDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookingOutDto.getId()))
                .andExpect(jsonPath("$.status").value(bookingOutDto.getStatus().toString()));
    }

    @Test
    void approvedBooking() throws Exception {
        bookingOutDto.setStatus(BookingStatus.APPROVED);

        when(bookingService.approved(eq(1L), eq(1L), eq(true)))
                .thenReturn(bookingOutDto);

        mvc.perform(patch("/bookings/1")
                        .header("X-Sharer-User-Id", 1L)
                        .param("approved", "true")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookingOutDto.getId()))
                .andExpect(jsonPath("$.status").value("APPROVED"));
    }

    @Test
    void getUserBookings() throws Exception {
        when(bookingService.getUserBookings(eq(1L), any(BookingState.class)))
                .thenReturn(List.of(bookingOutDto));

        mvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .param("state", "ALL")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(bookingOutDto.getId()));
    }

    @Test
    void getOwnerBookings() throws Exception {
        when(bookingService.getOwnerBookings(eq(1L), any(BookingState.class)))
                .thenReturn(List.of(bookingOutDto));

        mvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", 1L)
                        .param("state", "ALL")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(bookingOutDto.getId()));
    }
}
