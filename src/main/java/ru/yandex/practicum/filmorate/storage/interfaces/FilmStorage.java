package ru.yandex.practicum.filmorate.storage.interfaces;

import ru.yandex.practicum.filmorate.models.Film;

import java.util.List;

public interface FilmStorage {

    List<Film> getFilmMap();

    Film createFilm(Film film);

    Film updateFilms(Film film);

    Film getFilmById(int id);
}
