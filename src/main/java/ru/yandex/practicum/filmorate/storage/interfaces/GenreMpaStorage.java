package ru.yandex.practicum.filmorate.storage.interfaces;

import ru.yandex.practicum.filmorate.models.Genre;
import ru.yandex.practicum.filmorate.models.MPA;

import java.util.List;

public interface GenreMpaStorage {

    List<Genre> getListGenre();

    Genre getGenreById(int id);

    List<MPA> getListMpa();

    MPA getMpaById(int id);
}
