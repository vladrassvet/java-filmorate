package ru.yandex.practicum.filmorate.validation;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.validationException.ValidationException;
import ru.yandex.practicum.filmorate.models.Film;
import java.time.LocalDate;

@Slf4j
public class ValidateFilms {

    public boolean validateName(Film film) {
        if (film == null || film.getName() == null) {
            log.error("Название фильма пустое!");
            throw new ValidationException("Название фильма пустое!");
        } else if (film.getName().length() == 0) {
            log.error("Название фильма пустое!");
            throw new ValidationException("Название фильма пустое!");
        } else {
            return true;
        }
    }

    public boolean validateDescription(Film film) {
        if (film == null || film.getDescription() == null) {
            log.error("Описание фильма равно null");
            throw new ValidationException("Описание фильма равно null");
        } else if (film.getDescription().length() > 200) {
            log.error("Описание фильма длиннее 200 знаков!");
            throw new ValidationException("Описание фильма длиннее 200 знаков!");
        } else {
            return true;
        }
    }

    public boolean validateReleaseDate(Film film) {
        if (film == null || film.getReleaseDate() == null) {
            log.error("Дата релиза фильма равна null");
            throw new ValidationException("Дата релиза фильма равна null");
        }
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
}
