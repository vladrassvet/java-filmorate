package ru.yandex.practicum.filmorate.services.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.models.Film;
import ru.yandex.practicum.filmorate.models.MPA;
import ru.yandex.practicum.filmorate.models.User;
import ru.yandex.practicum.filmorate.storage.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class FilmDbStorageTest {
    private final FilmDbStorage filmStorage;
    private final UserDbStorage userDbStorage;

    MPA mpa = MPA.builder()
            .id(1)
            .name("G")
            .build();

    @Test
    void testCreateFilm() {
        Film film1 = new Film("name", "Description",
                LocalDate.of(1967, 3, 25), 100, mpa);
        filmStorage.createFilm(film1);
        assertThat(film1).hasFieldOrPropertyWithValue("id", 1);
        assertThat(film1).hasFieldOrPropertyWithValue("name", "name");
    }

    @Test
    void updateFilmTest() {
        Film film1 = new Film("name2", "description2",
                LocalDate.of(1967, 5, 25), 100, mpa);
        filmStorage.createFilm(film1);
        assertThat(film1).hasFieldOrPropertyWithValue("id", 1);
        film1 = new Film(1, "name2", "description2",
                LocalDate.of(1967, 5, 25), 100, mpa);
        filmStorage.updateFilms(film1);
        assertThat(film1).hasFieldOrPropertyWithValue("id", 1);
        assertThat(film1).hasFieldOrPropertyWithValue("name", "name2");

    }

    @Test
    void getFilmMapTest() {
        Film film1 = new Film("name", "description",
                LocalDate.of(1967, 3, 25), 100, mpa);
        Film film2 = new Film(1, "name2", "description2",
                LocalDate.of(1967, 5, 25), 100, mpa);
        filmStorage.createFilm(film1);
        filmStorage.createFilm(film2);
        List<Film> filmList = filmStorage.getFilmMap();
        assertEquals(2, filmList.size(), "неверное количество фильмов");
    }

    @Test
    void getFilmByIdTest() {
        Film film1 = new Film("name", "description",
                LocalDate.of(1967, 3, 25), 100, mpa);
        Film film2 = new Film(1, "name2", "description2",
                LocalDate.of(1967, 5, 25), 100, mpa);
        filmStorage.createFilm(film1);
        filmStorage.createFilm(film2);
        Film getFilm = filmStorage.getFilmById(2);
        assertEquals(2, getFilm.getId(), "фильм вернулся неверный");
        assertThat(getFilm).hasFieldOrPropertyWithValue("name", "name2");

    }

    @Test
    void addLikeAndDelTest() {
        User user = new User("user@email.ru", "vanya123",
                "Ivan Petrov", LocalDate.of(1990, 1, 1));
        Film film1 = new Film("name", "description",
                LocalDate.of(1967, 3, 25), 100, mpa);
        userDbStorage.createUser(user);
        filmStorage.createFilm(film1);
        filmStorage.addLike(1, 1);
        Film likeFilm = filmStorage.getFilmById(1);
        assertThat(likeFilm).hasFieldOrPropertyWithValue("like", 1);
        filmStorage.deleteLike(1, 1);
        Film likeFilm1 = filmStorage.getFilmById(1);
        assertThat(likeFilm1).hasFieldOrPropertyWithValue("like", 0);
    }
}
