package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.model.User;

import java.util.Collection;

public interface UserRepository {
    User create(User user);

    User update(Long userId, User user);

    void delete(Long userId);

    User findById(Long userId);

    Collection<User> findAll();
}
