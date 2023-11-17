package ru.yandex.practicum.filmorate.validation;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.validationException.ValidationException;
import ru.yandex.practicum.filmorate.models.User;

import java.time.LocalDate;

@Slf4j
public class UserValidator {

    public static void validateEmail(User user) {
        if (user == null || user.getEmail() == null) {
            log.error("Электронная почта пользователя равна null!");
            throw new ValidationException("Электронная почта пользователя равна null!");
        } else if (user.getEmail().isEmpty() || !user.getEmail().contains("@")) {
            log.error("Указана неверная электронная почта пользователя!");
            throw new ValidationException("Указана неверная электронная почта пользователя!");
        }
    }

    public static void validateLogin(User user) {
        if (user == null || user.getLogin() == null) {
            log.error("Логин пользователя равен null!");
            throw new ValidationException("Логин пользователя равен null!");
        }
        if (user.getLogin().isBlank()) {
            log.error("Указан неверный логин пользователя!");
            throw new ValidationException("Неверный login");
        }
    }

    public static User validateName(User user) {
        if (user == null) {
            log.error("Пользователь равен null, валидация имени невозможна!");
            throw new ValidationException("Пользователь равен null, валидация имени невозможна!");
        } else if (user.getName() == null) {
            user.setName(user.getLogin());
        } else if (user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        return user;
    }

    public static void validateBirthday(User user) {
        if (user == null || user.getBirthday() == null) {
            log.error("Дата рождения пользователя равна null!");
            throw new ValidationException("Дата рождения пользователя равна null!");
        } else if (user.getBirthday().isAfter(LocalDate.now())) {
            log.error("Дата рождения пользователя не может быть в будущем!");
            throw new ValidationException("Дата рождения пользователя не может быть в будущем!");
        }
    }
}
