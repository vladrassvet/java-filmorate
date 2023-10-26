package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controllers.UserController;
import ru.yandex.practicum.filmorate.services.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.validationException.NotFoundException;
import ru.yandex.practicum.filmorate.validationException.ValidationException;
import ru.yandex.practicum.filmorate.models.User;
import ru.yandex.practicum.filmorate.validation.UserValidator;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FilmorateApplicationUsersTests {

    private UserController userController;
    private UserValidator userValidator;
    private UserService userService;
    private UserStorage userStorage;
    private User user1;
    private User user2;

    @BeforeEach
    void init() {
        userStorage = new InMemoryUserStorage();
        userService = new UserService(userStorage);
        userController = new UserController(userService);
        userValidator = new UserValidator();

        user1 = new User();
        user1.setId(1);
        user1.setName("Name 1");
        user1.setBirthday(LocalDate.parse("1980-10-10"));
        user1.setLogin("Login 1");
        user1.setEmail("me@home.com");

        user2 = new User();
        user2.setId(2);
        user2.setName("Name 2");
        user2.setBirthday(LocalDate.parse("1981-10-10"));
        user2.setLogin("Login 2");
        user2.setEmail("haha@haha.com");
    }

    @Test
    void testUserEmailValidation() {
        user1.setEmail("meme.com");
        Exception exception = assertThrows(ValidationException.class, () -> userValidator.validateEmail(user1));
        assertEquals("Указана неверная электронная почта пользователя!", exception.getMessage());
    }

    @Test
    void testUserLoginValidation() {
        user1.setLogin("");
        Exception exception = assertThrows(ValidationException.class, () -> userValidator.validateLogin(user1));
        assertEquals("Указан неверный логин пользователя!", exception.getMessage());
    }

    @Test
    void testUserNameValidation() {
        user1.setName("");
        User testUser = userValidator.validateName(user1);
        assertEquals(user1.getLogin(), testUser.getName());
    }

    @Test
    void testUserBirthdayValidation() {
        user1.setBirthday(LocalDate.parse("2025-10-10"));
        Exception exception = assertThrows(ValidationException.class, () -> userValidator.validateBirthday(user1));
        assertEquals("Дата рождения пользователя не может быть в будущем!", exception.getMessage());
    }

    @Test
    void testUserCreation() {
        userController.addUser(user1);
        assertEquals(1, userStorage.getUserMap().size());
        userController.addUser(user2);
        assertEquals(2, userStorage.getUserMap().size());
    }

    @Test
    void testUserUpdate() {
        userController.addUser(user1);
        user1.setName("New Name");
        userController.updateUser(user1);
        assertEquals("New Name", userStorage.getUserMap().get(0).getName());
    }

    @Test
    void testUserUpdateUnknownId() {
        userController.addUser(user1);
        user1.setId(999);
        Exception exception = assertThrows(NotFoundException.class, () -> userController.updateUser(user1));
        assertEquals("пользователя с id = 999 не существует", exception.getMessage());
    }

    @Test
    void testUsersGet() {
        userController.addUser(user1);
        assertEquals(1, userController.getAll().size());
        userController.addUser(user2);
        assertEquals(2, userController.getAll().size());
    }
}

