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

    public List<User> getUserMap() {
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
        User user1 = userStorage.getUserById(friendId);
        Set<Integer> listFriendId = user.getFriendsId();
        Set<Integer> listFriendId1 = user1.getFriendsId();
        listFriendId.add(friendId);
        listFriendId1.add(id);
        log.info("друзья успешно добавлены");
        user.setFriendsId(listFriendId);
        user1.setFriendsId(listFriendId1);
    }

    public void deleteFriends(int id, int friendId) {
        User user = userStorage.getUserById(id);
        Set<Integer> listFriendId = user.getFriendsId();
        listFriendId.remove(friendId);
        user.setFriendsId(listFriendId);
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