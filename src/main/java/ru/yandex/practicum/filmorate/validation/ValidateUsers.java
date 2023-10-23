package ru.yandex.practicum.filmorate.validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yandex.practicum.filmorate.controllers.UserController;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.models.User;

import java.time.LocalDate;

public class ValidateUsers {

    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);
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
}
