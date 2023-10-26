package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.services.UserService;
import ru.yandex.practicum.filmorate.models.User;
import ru.yandex.practicum.filmorate.validation.UserValidator;

import java.util.*;

@RestController
@Slf4j
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public List<User> getAll() {
        return userService.getUsers();
    }

    @PostMapping("/users")
    public User addUser(@RequestBody User user) {
        UserValidator.validateBirthday(user);
        UserValidator.validateEmail(user);
        UserValidator.validateLogin(user);
        UserValidator.validateName(user);

        return userService.createUser(user);
    }

    @PutMapping("/users")
    public User updateUser(@RequestBody User user) {
        return userService.updateUsers(user);
    }

    @PutMapping("/users/{id}/friends/{friendId}")
    public void addFriend(@PathVariable int id, @PathVariable int friendId) {
        userService.addFriends(id, friendId);
    }

    @DeleteMapping("/users/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable int id, @PathVariable int friendId) {
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
