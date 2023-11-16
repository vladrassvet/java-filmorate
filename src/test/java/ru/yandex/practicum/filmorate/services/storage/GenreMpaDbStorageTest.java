package ru.yandex.practicum.filmorate.services.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.models.Genre;
import ru.yandex.practicum.filmorate.models.MPA;
import ru.yandex.practicum.filmorate.storage.GenreMpaDbStorage;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class GenreMpaDbStorageTest {

    private final GenreMpaDbStorage genreMpaDbStorage;

    @Test
    void testGetListMpa() {
        List<MPA> mpa = genreMpaDbStorage.getListMpa();
        assertEquals(5, mpa.size());
        assertEquals("G", mpa.get(0).getName());
        assertEquals(1, mpa.get(0).getId());
    }

    @Test
    void testGetByIdMpa() {
        MPA mpa = genreMpaDbStorage.getMpaById(2);
        assertEquals(2, mpa.getId());
        assertEquals("PG", mpa.getName());
    }

    @Test
    void testGetListGenres() {
        List<Genre> genres = genreMpaDbStorage.getListGenre();
        assertEquals(6, genres.size());
        assertEquals("Комедия", genres.get(0).getName());
        assertEquals(1, genres.get(0).getId());
    }

    @Test
    void testGetByIdGenre() {
        Genre genre = genreMpaDbStorage.getGenreById(2);
        assertEquals(2, genre.getId());
        assertEquals("Драма", genre.getName());

    }
}
