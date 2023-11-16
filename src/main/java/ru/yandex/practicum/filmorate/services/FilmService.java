package ru.yandex.practicum.filmorate.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.models.Film;
import ru.yandex.practicum.filmorate.storage.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {

    private final FilmStorage filmStorage;
    private final FilmDbStorage filmDbStorage;

    public List<Film> getFilmMap() {
        return filmStorage.getFilmMap();
    }

    public Film createFilm(Film film) {
        return filmStorage.createFilm(film);
    }

    public Film updateFilms(Film film) {
        return filmStorage.updateFilms(film);
    }

    public Film getFilmById(int id) {
        return filmStorage.getFilmById(id);
    }

    public void addLike(int id, int userId) {
        filmDbStorage.addLike(id, userId);
    }

    public void deleteLike(int id, int userId) {
        filmDbStorage.deleteLike(id, userId);
    }

    public List<Film> topLikeFilms(int count) {
        return filmDbStorage.topLikesFilms(count);
    }
}
