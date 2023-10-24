package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.services.UserService;
import ru.yandex.practicum.filmorate.validationException.NotFoundException;
import ru.yandex.practicum.filmorate.models.User;
import ru.yandex.practicum.filmorate.validation.ValidateUsers;

import java.util.*;

@RestController
@Slf4j
public class UserController {

    private UserService userService;
    private ValidateUsers validateUsers = new ValidateUsers();

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public List<User> getAll() {
        return userService.getUserMap();
    }

    @PostMapping("/users")
    public User addUser(@RequestBody User user) {
        validateUsers.validateBirthday(user);
        validateUsers.validateEmail(user);
        validateUsers.validateLogin(user);
        validateUsers.validateName(user);

        return userService.createUser(user);
    }

    @PutMapping("/users")
    public User updateUser(@RequestBody User user) {
        return userService.updateUsers(user);
    }

    @PutMapping("/users/{id}/friends/{friendId}")
    public void addFriend(@PathVariable int id, @PathVariable int friendId) {
        if (id <= 0 || friendId <= 0) {
            throw new NotFoundException("передан некорректный id; "
                    + id + "пользователя или добавляемого друга friendId; " + friendId);
        }
        userService.addFriends(id, friendId);
    }

    @DeleteMapping("/users/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable int id, @PathVariable int friendId) {
        if (id <= 0 || friendId <= 0) {
            throw new NotFoundException("передан некорректный id; "
                    + id + "пользователя или добавляемого друга friendId; " + friendId);
        }
        userService.deleteFriends(id, friendId);
    }

    @GetMapping("/users/{id}/friends")
    public Collection<User> getFriend(@PathVariable int id) {
        return userService.getFriends(id);
    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    public Collection<User> getFriendOfFriend(@PathVariable int id, @PathVariable int otherId) {
        return userService.getFriendOfFriends(id, otherId);
    }

    @GetMapping("/users/{id}")
    public User getUserById(@PathVariable int id) {
        return userService.getUserById(id);
    }
}
