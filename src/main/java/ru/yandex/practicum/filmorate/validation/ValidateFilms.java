package ru.yandex.practicum.filmorate.validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yandex.practicum.filmorate.controllers.FilmController;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.models.Film;

import java.time.LocalDate;

public class ValidateFilms {

    private static final Logger LOG = LoggerFactory.getLogger(FilmController.class);

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
}
