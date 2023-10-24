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

    public List<Film> getFilmMap() {
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
        Set<Integer> listLikes = film.getLikes();
        listLikes.add(userId);
        log.info("like успешно добавлен");
        film.setLikes(listLikes);
    }

    public void deleteLike(int id, int userId) {
        Film film = filmStorage.getFilmById(id);
        Set<Integer> listLikes = film.getLikes();
        listLikes.remove(userId);
        log.info("like успешно удален");
        film.setLikes(listLikes);
    }

    public List<Film> topLikeFilms(int count) {
        List<Film> listFilms = getFilmMap();
        if (count > listFilms.size()) {
            count = listFilms.size();
        }
        log.info("топ фильмов {}", listFilms);
        return listFilms.stream()
                .sorted((p0, p1) -> {
                    int comp = compare(p0.getLikes().size(), p1.getLikes().size());
                    return -1 * comp;
                }).limit(count)
                .collect(Collectors.toList());
    }

    public Film getFilmById(int id) {
        return filmStorage.getFilmById(id);
    }
}
