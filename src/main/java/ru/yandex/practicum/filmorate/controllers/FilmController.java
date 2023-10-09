package ru.yandex.practicum.filmorate.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.models.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/films")
public class FilmController {

    private static final Logger LOG = LoggerFactory.getLogger(FilmController.class);
    private Map<Integer, Film> list = new HashMap<>();

    @GetMapping
    public List<Film> getAll() {
        LOG.debug("Текущее количество фильмов в базе: {}", list.size());
        List<Film> toReturn = new ArrayList<>();
        toReturn.addAll(list.values());
        return toReturn;
    }

    @PostMapping
    public Film post(@RequestBody Film film) {
        if (validateName(film) && validateDuration(film) && validateDescription(film) && validateReleaseDate(film)) {
            LOG.debug("Фильм прошёл валидацию и добавлен в базу");
            if (film.getId() == 0)
                film.setId(1);
            list.put(film.getId(), film);
        }
        return film;
    }

    @PutMapping
    public Film put(@RequestBody Film film) {
        Film newFilm = null;
        if (validateName(film) && validateDuration(film) && validateDescription(film) && validateReleaseDate(film)) {
            LOG.debug("Фильм прошёл валидацию при обновлении");
            if (list.containsKey(film.getId())) {
                list.remove(film.getId());
                newFilm = new Film();
                newFilm.setId(film.getId());
                newFilm.setName(film.getName());
                newFilm.setDescription(film.getDescription());
                newFilm.setReleaseDate(film.getReleaseDate());
                newFilm.setDuration(film.getDuration());
                list.put(newFilm.getId(), newFilm);
                LOG.debug("Фильм обновлён в базе данных");
            } else {
                LOG.error("Фильм {} не найден в базе данных", film.getName());
                throw new ValidationException("Фильм \"" + film.getName() + "\" не найден в базе данных");
            }
        }
        return newFilm;
    }

    public boolean validateName(Film film) {
        if (film == null || film.getName() == null) {
            LOG.error("Название фильма пустое!");
            throw new ValidationException("Название фильма пустое!");
        } else if (film.getName().length() == 0) {
            LOG.error("Название фильма пустое!");
            throw new ValidationException("Название фильма пустое!");
        } else {
            return true;
        }
    }

    public boolean validateDescription(Film film) {
        if (film == null || film.getDescription() == null) {
            LOG.error("Описание фильма равно null");
            throw new ValidationException("Описание фильма равно null");
        } else if (film.getDescription().length() > 200) {
            LOG.error("Описание фильма длиннее 200 знаков!");
            throw new ValidationException("Описание фильма длиннее 200 знаков!");
        } else {
            return true;
        }
    }

    public boolean validateReleaseDate(Film film) {
        if (film == null || film.getReleaseDate() == null) {
            LOG.error("Дата релиза фильма равна null");
            throw new ValidationException("Дата релиза фильма равна null");
        }
        if (film.getReleaseDate().isBefore(LocalDate.parse("1895-12-28"))) {
            LOG.error("Дата релиза фильма должна быть не раньше 28 декабря 1895 года!");
            throw new ValidationException("Дата релиза фильма должна быть не раньше 28 декабря 1895 года!");
        } else {
            return true;
        }
    }

    public boolean validateDuration(Film film) {
        if (film.getDuration() <= 0) {
            LOG.error("Продолжительность фильма должна быть положительной");
            throw new ValidationException("Продолжительность фильма должна быть положительной");
        } else {
            return true;
        }
    }

    public Map<Integer, Film> returnList() {
        return list;
    }
}
