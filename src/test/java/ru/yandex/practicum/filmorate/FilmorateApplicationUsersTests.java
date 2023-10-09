package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controllers.UserController;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.models.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FilmorateApplicationUsersTests {

    private UserController userController;
    private User user1;
    private User user2;

    @BeforeEach
    void init() {
        userController = new UserController();

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
        Exception exception = assertThrows(ValidationException.class, () -> userController.validateEmail(user1));
        assertEquals("Указана неверная электронная почта пользователя!", exception.getMessage());
    }

    @Test
    void testUserLoginValidation() {
        user1.setLogin("");
        Exception exception = assertThrows(ValidationException.class, () -> userController.validateLogin(user1));
        assertEquals("Указан неверный логин пользователя!", exception.getMessage());
    }

    @Test
    void testUserNameValidation() {
        user1.setName("");
        User testUser = userController.validateName(user1);
        assertEquals(user1.getLogin(), testUser.getName());
    }

    @Test
    void testUserBirthdayValidation() {
        user1.setBirthday(LocalDate.parse("2025-10-10"));
        Exception exception = assertThrows(ValidationException.class, () -> userController.validateBirthday(user1));
        assertEquals("Дата рождения пользователя не может быть в будущем!", exception.getMessage());
    }

    @Test
    void testUserCreation() {
        userController.post(user1);
        assertEquals(1, userController.returnList().size());
        userController.post(user2);
        assertEquals(2, userController.returnList().size());
    }

    @Test
    void testUserUpdate() {
        userController.post(user1);
        user1.setName("New Name");
        userController.put(user1);
        assertEquals("New Name", userController.returnList().get(user1.getId()).getName());
    }

    @Test
    void testUserUpdateUnknownId() {
        userController.post(user1);
        user1.setId(999);
        Exception exception = assertThrows(ValidationException.class, () -> userController.put(user1));
        assertEquals("Пользователь \"Name 1\" не найден в базе данных", exception.getMessage());
    }

    @Test
    void testUsersGet() {
        userController.post(user1);
        assertEquals(1, userController.getAll().size());
        userController.post(user2);
        assertEquals(2, userController.getAll().size());
    }
}

