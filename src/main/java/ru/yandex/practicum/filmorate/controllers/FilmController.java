package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.models.Film;
import ru.yandex.practicum.filmorate.validation.ValidateFilms;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private static final Logger LOG = LoggerFactory.getLogger(FilmController.class);
    private Map<Integer, Film> list = new HashMap<>();
    private ValidateFilms validateFilms = new ValidateFilms();

    @GetMapping
    public List<Film> getAll() {
        LOG.debug("Текущее количество фильмов в базе: {}", list.size());
        return new ArrayList<>(list.values());
    }

    @PostMapping
    public Film addFilm(@RequestBody Film film) {
        if (validateFilms.validateName(film) && validateFilms.validateDuration(film) &&
                validateFilms.validateDescription(film) && validateFilms.validateReleaseDate(film)) {
            LOG.debug("Фильм прошёл валидацию и добавлен в базу");
            if (film.getId() == 0)
                film.setId(1);
            list.put(film.getId(), film);
        }
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        Film newFilm = null;
        if (validateFilms.validateName(film) && validateFilms.validateDuration(film) &&
                validateFilms.validateDescription(film) && validateFilms.validateReleaseDate(film)) {
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

    public Map<Integer, Film> returnList() {
        return list;
    }
}
