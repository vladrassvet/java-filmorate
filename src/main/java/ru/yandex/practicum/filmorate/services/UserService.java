package ru.yandex.practicum.filmorate.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.models.User;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userStorage;

    public List<User> getUserMap() {
        return userStorage.getUserMap();
    }

    public User createUser(User user) {
        return userStorage.createUser(user);
    }

    public User updateUsers(User user) {
        return userStorage.updateUsers(user);
    }

    public User getUserById(int id) {
        return userStorage.getUserById(id);
    }

    public void addFriends(int id, int friendId) {
        userStorage.addFriend(id, friendId);
    }

    public void deleteFriends(int id, int friendId) {
        userStorage.deleteFriend(id, friendId);
    }

    public List<User> getFriends(int id) {
        return userStorage.printFriend(id);
    }

    public List<User> getFriendOfFriends(int id, int otherId) {
        return userStorage.getFriendOfFriends(id, otherId);
    }
}
