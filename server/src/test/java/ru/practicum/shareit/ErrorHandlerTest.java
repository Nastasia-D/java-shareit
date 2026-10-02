package ru.practicum.shareit;

import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import ru.practicum.shareit.exception.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ErrorHandlerTest {

    private final ErrorHandler errorHandler = new ErrorHandler();

    @Test
    void handleValidationException() {
        Exception e = new Exception("Validation error");
        ErrorResponse response = errorHandler.handlerValidationException(e);
        assertNotNull(response);
        assertEquals("Некорректное значение параметра", response.getError());
    }

    @Test
    void handleNotFoundException() {
        NotFoundException e = new NotFoundException("Not found");
        ErrorResponse response = errorHandler.handlerNotFoundException(e);
        assertNotNull(response);
        assertEquals("Объект не найден", response.getError());
    }

    @Test
    void handleThrowable() {
        Throwable e = new Throwable("Fatal error");
        ErrorResponse response = errorHandler.handlerThrowable(e);
        assertNotNull(response);
        assertEquals("Произошла непредвиденная ошибка.", response.getError());
    }

    @Test
    void handleParameterNotValidException() {
        ParameterNotValidException e = new ParameterNotValidException("Invalid param", "Reason");
        ErrorResponse response = errorHandler.handlerParameterNotValidException(e);
        assertNotNull(response);
        assertEquals("Некорректное значение параметра", response.getError());
    }

    @Test
    void handleConflictException() {
        ConflictException e = new ConflictException("Conflict");
        ErrorResponse response = errorHandler.handleConflictException(e);
        assertNotNull(response);
        assertEquals("Конфликт данных", response.getError());
    }

    @Test
    void handleDataIntegrityViolation() {
        DataIntegrityViolationException e = new DataIntegrityViolationException("DB error");
        ErrorResponse response = errorHandler.handleDataIntegrityViolation(e);
        assertNotNull(response);
        assertEquals("Конфликт данных", response.getError());
    }

    @Test
    void handleForbiddenException() {
        ForbiddenException e = new ForbiddenException("Forbidden");
        ErrorResponse response = errorHandler.handleForbiddenException(e);
        assertNotNull(response);
        assertEquals("Доступ запрещён", response.getError());
    }
}
