package ru.yandex.practicum.filmorate.services.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.models.User;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UserDbStorageTest {

    private final UserDbStorage userDbStorage;

    @Test
    public void testFindUserById() {
        User user = new User("user@email.ru", "vanya123",
                "Ivan Petrov", LocalDate.of(1990, 1, 1));
        userDbStorage.createUser(user);
        User savedUser = userDbStorage.getUserById(1);
        assertThat(savedUser)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(user);
    }

    @Test
    public void updateUserTest() {
        User user = new User("user@email.ru", "vanya123",
                "Ivan Petrov", LocalDate.of(1990, 1, 1));
        userDbStorage.createUser(user);
        assertThat(user).hasFieldOrPropertyWithValue("id", 1);
        assertThat(user).hasFieldOrPropertyWithValue("login", "vanya123");

        user = new User(1, "user@email.ru", "vanya12345",
                "Ivan Petrov", LocalDate.of(1990, 5, 1));
        userDbStorage.updateUsers(user);
        assertThat(user).hasFieldOrPropertyWithValue("id", 1);
        assertThat(user).hasFieldOrPropertyWithValue("login", "vanya12345");
    }

    @Test
    void getMapUserTest() {
        User user = new User("user@email.ru", "vanya123",
                "Ivan Petrov", LocalDate.of(1990, 1, 1));
        User user1 = new User("user@email.ru", "vanya1234",
                "Ivan Petrovs", LocalDate.of(1990, 10, 1));
        userDbStorage.createUser(user);
        userDbStorage.createUser(user1);
        List<User> userList = userDbStorage.getUserMap();
        assertEquals(2, userList.size(), "неверное количество пользователей");
        assertThat(userList.get(1)).hasFieldOrPropertyWithValue("name", "Ivan Petrovs");
    }

    @Test
    void addFriendTest() {
        User user = new User("user@email.ru", "vanya123",
                "Ivan Petrov", LocalDate.of(1990, 1, 1));
        User user1 = new User("user@email.ru", "vanya1234",
                "Ivan Petrovs", LocalDate.of(1990, 10, 1));
        userDbStorage.createUser(user);
        userDbStorage.createUser(user1);
        userDbStorage.addFriend(1, 2);
        List<User> listFriends = userDbStorage.printFriend(1);
        assertThat(listFriends.get(0)).hasFieldOrPropertyWithValue("id", 2);
    }

    @Test
    void deleteFriendTest() {
        User user = new User("user@email.ru", "vanya123",
                "Ivan Petrov", LocalDate.of(1990, 1, 1));
        User user1 = new User("user@email.ru", "vanya1234",
                "Ivan Petrovs", LocalDate.of(1990, 10, 1));
        userDbStorage.createUser(user);
        userDbStorage.createUser(user1);
        userDbStorage.addFriend(1, 2);
        List<User> listFriends = userDbStorage.printFriend(1);
        assertThat(listFriends.get(0)).hasFieldOrPropertyWithValue("id", 2);
        assertEquals(1, listFriends.size(), "пользователь имеет не верное количество друзей");
        userDbStorage.deleteFriend(1, 2);
        List<User> listFriends1 = userDbStorage.printFriend(1);
        assertEquals(0, listFriends1.size(), "друзья не удалились");
    }
}
