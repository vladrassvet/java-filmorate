package ru.yandex.practicum.filmorate.storage.interfaces;

import ru.yandex.practicum.filmorate.models.User;

import java.util.List;

public interface UserStorage {

    User getUserById(int id);

    List<User> getUserMap();

    User createUser(User user);

    User updateUsers(User user);
}
