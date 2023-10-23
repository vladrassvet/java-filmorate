package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.models.User;
import ru.yandex.practicum.filmorate.validation.ValidateUsers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);
    private Map<Integer, User> list = new HashMap<>();
    private ValidateUsers validateUsers = new ValidateUsers();

    @GetMapping
    public List<User> getAll() {
        LOG.debug("Текущее количество пользователей в базе: {}", list.size());
        return new ArrayList<>(list.values());
    }

    @PostMapping
    public User addUser(@RequestBody User user) {
        if (validateUsers.validateBirthday(user) && validateUsers.validateEmail(user) &&
                validateUsers.validateLogin(user)) {
            if (list.size() == 0) {
                user.setId(1);
            } else {
                user.setId(2);
            }
            validateUsers.validateName(user);
            list.put(user.getId(), user);
            LOG.debug("Пользователь успешно прошёл валидацию и добавлен в базу");
        }
        return user;
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        User newUser = null;
        if (list.containsKey(user.getId())) {
            if (validateUsers.validateBirthday(user) && validateUsers.validateEmail(user) &&
                    validateUsers.validateLogin(user)) {
                validateUsers.validateName(user);
                newUser = new User();
                newUser.setId(user.getId());
                newUser.setName(user.getName());
                newUser.setEmail(user.getEmail());
                newUser.setLogin(user.getLogin());
                newUser.setBirthday(user.getBirthday());

                list.remove(user.getId());
                list.put(user.getId(), newUser);
                LOG.debug("Пользователь прошёл валидацию, обновлён и добавлен в базу");
            }
        } else {
            LOG.error("Пользователь {} не найден в базе данных", user.getName());
            throw new ValidationException("Пользователь \"" + user.getName() + "\" не найден в базе данных");
        }
        return newUser;
    }

    public Map<Integer, User> returnList() {
        return list;
    }
}
