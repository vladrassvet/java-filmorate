package ru.yandex.practicum.filmorate.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.models.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userStorage;

    public List<User> getUsers() {
        return userStorage.getUserMap();
    }

    public User createUser(User user) {
        return userStorage.createUser(user);
    }

    public User updateUsers(User user) {
        return userStorage.updateUsers(user);
    }

    public void addFriends(int id, int friendId) {
        User user = userStorage.getUserById(id);
        User friend = userStorage.getUserById(friendId);
        user.getFriendsId().add(friendId);
        friend.getFriendsId().add(id);
        log.info("друзья успешно добавлены");
    }

    public void deleteFriends(int id, int friendId) {
        User user = userStorage.getUserById(id);
        user.getFriendsId().remove(friendId);
        log.info("друг с id " + friendId + " успешно удален");
    }

    public List<User> getFriends(int id) {
        User user = userStorage.getUserById(id);
        Set<Integer> listFriendId = user.getFriendsId();
        List<User> getFriend = new ArrayList<>();
        for (Integer integer : listFriendId) {
            getFriend.add(userStorage.getUserById(integer));
        }
        log.info("друзья USERa с id " + id + " {}", getFriend);
        return getFriend;
    }

    public List<User> getFriendOfFriends(int id, int otherId) {
        List<User> userList = getFriends(id).stream()
                .filter(getFriends(otherId)::contains)
                .collect(Collectors.toList());
        log.info("общие друзья USERa с id: " + id + " USERa c id: " + otherId + "{}", userList);
        return userList;
    }

    public User getUserById(int id) {
        return userStorage.getUserById(id);
    }
}