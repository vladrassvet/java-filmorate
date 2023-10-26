package ru.yandex.practicum.filmorate.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.models.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Integer.compare;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {

    private final FilmStorage filmStorage;

    public List<Film> getFilms() {
        return filmStorage.getFilmMap();
    }

    public Film createFilm(Film film) {
        return filmStorage.createFilm(film);
    }

    public Film updateFilms(Film film) {
        return filmStorage.updateFilms(film);
    }

    public void addLike(int id, int userId) {
        Film film = filmStorage.getFilmById(id);
        film.getLikes().add(userId);
        log.info("like успешно добавлен");
    }

    public void deleteLike(int id, int userId) {
        Film film = filmStorage.getFilmById(id);
        film.getLikes().remove(userId);
        log.info("like успешно удален");
    }

    public List<Film> topLikeFilms(int count) {
        List<Film> listFilms = getFilms();
        if (count > listFilms.size()) {
            count = listFilms.size();
        }
        log.info("топ фильмов {}", listFilms);
        return listFilms.stream()
                .sorted((p0, p1) -> compare(p1.getLikes().size(), p0.getLikes().size())).limit(count)
                .collect(Collectors.toList());
    }

    public Film getFilmById(int id) {
        return filmStorage.getFilmById(id);
    }
}
