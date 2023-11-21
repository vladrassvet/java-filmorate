package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.models.User;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;
import ru.yandex.practicum.filmorate.validationException.ChangeException;
import ru.yandex.practicum.filmorate.validationException.NotFoundException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@Primary
@Component
@Slf4j
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public User createUser(User user) {
        String sqlQuery = "insert into users (email, login, name, birthday) " + "values (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
                    PreparedStatement prp = connection.prepareStatement(sqlQuery, new String[]{"user_id"});
                    prp.setString(1, user.getEmail());
                    prp.setString(2, user.getLogin());
                    prp.setString(3, user.getName());
                    prp.setObject(4, user.getBirthday());
                    return prp;
                }, keyHolder
        );
        int id = keyHolder.getKey().intValue();
        user.setId(id);
        return user;
    }

    @Override
    public User updateUsers(User user) {
        String sqlQuery = "update users set email = ?, login = ?, name = ?, birthday = ? where user_id = ?";
        jdbcTemplate.update(sqlQuery, user.getEmail(), user.getLogin(),
                user.getName(), user.getBirthday(), user.getId());
        return getUserById(user.getId());
    }

    @Override
    public User getUserById(int id) {
        String sqlQuery = "select * from users where user_id = ?";
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(sqlQuery, id);
        if (userRows.next()) {
            log.info("По заданному id найден пользователь: {} {}",
                    userRows.getString("user_id"), userRows.getString("login"));
            return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToUser, id);
        } else {
            log.warn("Пользователь с идентификатором {} не найден.", id);
            throw new NotFoundException("Такого пользователя не существует");
        }
    }

    @Override
    public List<User> getUserMap() {
        String sqlQuery = "select * from users";
        return jdbcTemplate.query(sqlQuery, this::mapRowToUser);
    }

    @Override
    public void addFriend(int id, int friendId) {
        if (getUserById(id) != null && getUserById(friendId) != null) {
            String sqlQuery = "select user_id from friends where friend_id = ? AND status = ?";
            List<Integer> applicationId = jdbcTemplate.queryForList(sqlQuery, Integer.class, id, "application");
            if (applicationId.contains(friendId)) {
                String sqlUpdate = "update friends set status = ? where user_id = ? and friend_id = ?";
                jdbcTemplate.update(sqlUpdate, "friends", friendId, id);
                String sqlAdd = "insert into friends (user_id, friend_id, status) values (?,?,?)";
                jdbcTemplate.update(sqlAdd, id, friendId, "friends");
                log.info("Образована дружба между пользователями {} и {}", id, friendId);
            } else {
                String sql = "insert into friends (user_id, friend_id, status) values (?, ?, ?)";
                jdbcTemplate.update(sql, id, friendId, "application");
            }
        } else {
            throw new ChangeException("Такого пользователя не существует");
        }
    }

    @Override
    public void deleteFriend(int id, int friendId) {
        String sqlQuery = "delete from friends where user_id = ? AND friend_id = ?";
        jdbcTemplate.update(sqlQuery, id, friendId);
        String sqlUpdate = "update public.friends set status = ? where user_id = ? AND friend_id = ?";
        jdbcTemplate.update(sqlUpdate, "application", id, friendId);
    }

    public List<User> printFriend(int id) {
        String sqlQuery = "select friend_id from friends where user_id = ?";
        List<Integer> listFriends = jdbcTemplate.queryForList(sqlQuery, Integer.class, id);
        return listFriends.stream()
                .map(this::getUserById)
                .collect(Collectors.toList());
    }

    public List<User> getFriendOfFriends(int id, int oderId) {
        return printFriend(id).stream()
                .filter(printFriend(oderId)::contains)
                .collect(Collectors.toList());
    }

    private User mapRowToUser(ResultSet resultSet, int i) throws SQLException {
        User user = new User(
                resultSet.getString("email"),
                resultSet.getString("login"),
                resultSet.getString("name"),
                resultSet.getDate("birthday").toLocalDate()
        );
        user.setId(resultSet.getInt("user_id"));
        return user;
    }
}
