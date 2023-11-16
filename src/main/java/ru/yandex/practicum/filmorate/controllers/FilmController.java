package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.services.FilmService;
import ru.yandex.practicum.filmorate.validationException.NotFoundException;
import ru.yandex.practicum.filmorate.models.Film;
import ru.yandex.practicum.filmorate.validation.FilmValidator;

import java.util.List;

@Slf4j
@RestController
public class FilmController {

    private FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping("/films")
    public List<Film> getAll() {
        return filmService.getFilmMap();
    }

    @PostMapping("/films")
    public Film addFilm(@RequestBody Film film) {
        FilmValidator.validateName(film);
        FilmValidator.validateDuration(film);
        FilmValidator.validateDescription(film);
        FilmValidator.validateReleaseDate(film);

        return filmService.createFilm(film);
    }

    @PutMapping("/films")
    public Film updateFilm(@RequestBody Film film) {
        return filmService.updateFilms(film);
    }

    @PutMapping("/films/{id}/like/{userId}")
    public void addLike(@PathVariable int id, @PathVariable int userId) {
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public void deleteLike(@PathVariable int id, @PathVariable int userId) {
       if (userId <= 0 || id <= 0) {
            throw new NotFoundException("передан некорректный id; " + id +
                    " фильма, или пользователя userId; " + userId);
        }
        filmService.deleteLike(id, userId);
    }

    @GetMapping("/films/popular")
    public List<Film> topLikesFilms(@RequestParam(defaultValue = "10") int count) {
        return filmService.topLikeFilms(count);
    }

    @GetMapping("/films/{id}")
    public Film getFilmById(@PathVariable int id) {
        return filmService.getFilmById(id);
    }
}
