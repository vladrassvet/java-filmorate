package ru.yandex.practicum.filmorate.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.core.Local;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.models.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/films")
public class FilmController {

    private static final Logger log = LoggerFactory.getLogger(FilmController.class);
    private HashMap<Integer, Film> list = new HashMap<>();

    @GetMapping
    public List<Film> get() {
        log.debug("Текущее количество фильмов в базе: {}", list.size());
        List<Film> toReturn = new ArrayList<>();
        for (Film film : list.values()) {
            toReturn.add(film);
        }
        return toReturn;
    }

    @PostMapping
    public Film post(@RequestBody Film film) {
        if (validateName(film) && validateDuration(film) && validateDescription(film) && validateReleaseDate(film)) {
            log.debug("Фильм прошёл валидацию и добавлен в базу");
            if(film.getId() == 0)
                film.setId(1);
            list.put(film.getId(), film);
        }
        return film;
    }

    @PutMapping
    public Film put(@RequestBody Film film) {
        Film newFilm = new Film();
        if (validateName(film) && validateDuration(film) && validateDescription(film) && validateReleaseDate(film)) {
            log.debug("Фильм прошёл валидацию при обновлении");
            if (list.containsKey(film.getId())) {
                list.remove(film.getId());
                newFilm.setId(film.getId());
                newFilm.setName(film.getName());
                newFilm.setDescription(film.getDescription());
                newFilm.setReleaseDate(film.getReleaseDate());
                newFilm.setDuration(film.getDuration());
                list.put(newFilm.getId(), newFilm);
                log.debug("Фильм обновлён в базе данных");
            } else {
                log.error("Фильм не найден в базе данных");
                throw new ValidationException("Фильм не найден в базе данных");
            }
        }
        return newFilm;
    }

    public boolean validateName(Film film) {
        if(film == null || film.getName() == null){
            log.error("Название фильма пустое!");
            throw new ValidationException("Название фильма пустое!");
        }
        else {
            if (film.getName().length() == 0) {
                log.error("Название фильма пустое!");
                throw new ValidationException("Название фильма пустое!");
            } else {
                return true;
            }
        }
    }

    public boolean validateDescription(Film film) {
        if (film.getDescription().length() > 200) {
            log.error("Описание фильма длиннее 200 знаков!");
            throw new ValidationException("Описание фильма длиннее 200 знаков!");
        } else {
            return true;
        }
    }

    public boolean validateReleaseDate(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.parse("1895-12-28"))) {
            log.error("Дата релиза фильма должна быть не раньше 28 декабря 1895 года!");
            throw new ValidationException("Дата релиза фильма должна быть не раньше 28 декабря 1895 года!");
        } else {
            return true;
        }
    }

    public boolean validateDuration(Film film) {
        if (film.getDuration() <= 0) {
            log.error("Продолжительность фильма должна быть положительной");
            throw new ValidationException("Продолжительность фильма должна быть положительной");
        } else {
            return true;
        }
    }

    public HashMap<Integer, Film> getHashMap(){
        return list;
    }
}
