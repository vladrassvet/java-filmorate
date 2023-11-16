package ru.yandex.practicum.filmorate.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.models.Genre;
import ru.yandex.practicum.filmorate.models.MPA;
import ru.yandex.practicum.filmorate.storage.interfaces.GenreMpaStorage;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GenreMpaService {
    private final GenreMpaStorage genreMpaStorage;

    public List<Genre> getListGenre() {
        return genreMpaStorage.getListGenre();
    }

    public Genre getGenreById(int id) {
        return genreMpaStorage.getGenreById(id);
    }

    public List<MPA> getListMpa() {
        return genreMpaStorage.getListMpa();
    }

    public MPA getMpaById(int id) {
        return genreMpaStorage.getMpaById(id);
    }
}
