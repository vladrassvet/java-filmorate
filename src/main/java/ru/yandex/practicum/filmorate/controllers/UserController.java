package ru.yandex.practicum.filmorate.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.models.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private HashMap<Integer, User> list = new HashMap<>();

    @GetMapping
    public List<User> get() {
        log.debug("Текущее количество пользователей в базе: {}", list.size());
        List<User> toReturn = new ArrayList<>();
        for (User user : list.values()) {
            toReturn.add(user);
        }
        return toReturn;
    }

    @PostMapping
    public User post(@RequestBody User user) {
        if(validateBirthday(user) && validateEmail(user) && validateLogin(user)){

            if(list.size() == 0)
                user.setId(1);
            else
                user.setId(2);

            validateName(user);
            list.put(user.getId(), user);
            log.debug("Пользователь успешно прошёл валидацию и добавлен в базу");
        }

        return user;
    }

    @PutMapping
    public User put(@RequestBody User user) {
        User newUser = new User();
        if(list.containsKey(user.getId())) {
            if (validateBirthday(user) && validateEmail(user) && validateLogin(user)) {
                validateName(user);

                newUser.setId(user.getId());
                newUser.setName(user.getName());
                newUser.setEmail(user.getEmail());
                newUser.setLogin(user.getLogin());
                newUser.setBirthday(user.getBirthday());

                list.remove(user.getId());
                list.put(user.getId(), newUser);
                log.debug("Пользователь прошёл валидацию, обновлён и добавлен в базу");
            }
        }
        else{
            log.error("Пользователь не найден в базе данных");
            throw new ValidationException("Пользователь не найден в базе данных");
        }
        return newUser;
    }

    public boolean validateEmail(User user) {
        if (user.getEmail().isEmpty() || !user.getEmail().contains("@")) {
            log.error("Указана неверная электронная почта пользователя!");
            throw new ValidationException("Указана неверная электронная почта пользователя!");
        } else {
            return true;
        }
    }

    public boolean validateLogin(User user) {
        if (user.getLogin().isBlank()) {
            log.error("Указан неверный логин пользователя!");
            throw new ValidationException("Указан неверный логин пользователя!");
        } else {
            return true;
        }
    }

    public User validateName(User user) {
        if(user == null || user.getName() == null){
            user.setName(user.getLogin());
        }
        else if (user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        return user;
    }

    public boolean validateBirthday(User user) {
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.error("Дата рождения пользователя не может быть в будущем!");
            throw new ValidationException("Дата рождения пользователя не может быть в будущем!");
        } else {
            return true;
        }
    }

    public HashMap<Integer, User> getHashMap(){
        return list;
    }
}
