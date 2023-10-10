package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.models.User;

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

    @GetMapping
    public List<User> getAll() {
        LOG.debug("Текущее количество пользователей в базе: {}", list.size());
        return new ArrayList<>(list.values());
    }

    @PostMapping
    public User addUser(@RequestBody User user) {
        if (validateBirthday(user) && validateEmail(user) && validateLogin(user)) {
            if (list.size() == 0){
                user.setId(1);
            }
            else{
                user.setId(2);
            }
            validateName(user);
            list.put(user.getId(), user);
            LOG.debug("Пользователь успешно прошёл валидацию и добавлен в базу");
        }
        return user;
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        User newUser = null;
        if (list.containsKey(user.getId())) {
            if (validateBirthday(user) && validateEmail(user) && validateLogin(user)) {
                validateName(user);
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

    public boolean validateEmail(User user) {
        if (user == null || user.getEmail() == null) {
            LOG.error("Электронная почта пользователя равна null!");
            throw new ValidationException("Электронная почта пользователя равна null!");
        } else if (user.getEmail().isEmpty() || !user.getEmail().contains("@")) {
            LOG.error("Указана неверная электронная почта пользователя!");
            throw new ValidationException("Указана неверная электронная почта пользователя!");
        } else {
            return true;
        }
    }

    public boolean validateLogin(User user) {
        if (user == null || user.getLogin() == null) {
            LOG.error("Логин пользователя равен null!");
            throw new ValidationException("Логин пользователя равен null!");
        }
        if (user.getLogin().isBlank()) {
            LOG.error("Указан неверный логин пользователя!");
            throw new ValidationException("Указан неверный логин пользователя!");
        } else {
            return true;
        }
    }

    public User validateName(User user) {
        if (user == null) {
            LOG.error("Пользователь равен null, валидация имени невозможна!");
            throw new ValidationException("Пользователь равен null, валидация имени невозможна!");
        } else if (user.getName() == null) {
            user.setName(user.getLogin());
        } else if (user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        return user;
    }

    public boolean validateBirthday(User user) {
        if (user == null || user.getBirthday() == null) {
            LOG.error("Дата рождения пользователя равна null!");
            throw new ValidationException("Дата рождения пользователя равна null!");
        } else if (user.getBirthday().isAfter(LocalDate.now())) {
            LOG.error("Дата рождения пользователя не может быть в будущем!");
            throw new ValidationException("Дата рождения пользователя не может быть в будущем!");
        } else {
            return true;
        }
    }

    public Map<Integer, User> returnList() {
        return list;
    }
}
