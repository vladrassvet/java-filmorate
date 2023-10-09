package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.models.Film;
import ru.yandex.practicum.filmorate.controllers.FilmController;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FilmorateApplicationFilmsTests {

    private FilmController filmController;
    private Film film1;
    private Film film2;

    @BeforeEach
    void init() {
        filmController = new FilmController();

        film1 = new Film();
        film1.setId(1);
        film1.setName("Name 1");
        film1.setDescription("Description 1");
        film1.setReleaseDate(LocalDate.parse("2000-10-10"));
        film1.setDuration(120);

        film2 = new Film();
        film2.setId(2);
        film2.setReleaseDate(LocalDate.parse("2010-10-01"));
        film2.setDuration(130);
        film2.setName("Name 2");
        film2.setDescription("Description 2");
    }

    @Test
    void testFilmDescriptionValidation() {
        final Film film = new Film();
        film.setDescription("Описание фильма длиннее 200 знаков!Описание фильма длиннее 200 знаков!" +
                "Описание фильма длиннее 200 знаков!Описание фильма длиннее 200 знаков!" +
                "Описание фильма длиннее 200 знаков!Описание фильма длиннее 200 знаков!" +
                "Описание фильма длиннее 200 знаков!Описание фильма длиннее 200 знаков!" +
                "Описание фильма длиннее 200 знаков!Описание фильма длиннее 200 знаков!" +
                "Описание фильма длиннее 200 знаков!Описание фильма длиннее 200 знаков!" +
                "Описание фильма длиннее 200 знаков!Описание фильма длиннее 200 знаков!" +
                "Описание фильма длиннее 200 знаков!Описание фильма длиннее 200 знаков!" +
                "Описание фильма длиннее 200 знаков!Описание фильма длиннее 200 знаков!" +
                "Описание фильма длиннее 200 знаков!Описание фильма длиннее 200 знаков!");
        Exception exception = assertThrows(ValidationException.class, () -> filmController.validateDescription(film));
        assertEquals("Описание фильма длиннее 200 знаков!", exception.getMessage());
    }

    @Test
    void testFilmDurationValidation() {
        final Film film = new Film();
        film.setDuration(-1);
        Exception exception = assertThrows(ValidationException.class, () -> filmController.validateDuration(film));
        assertEquals("Продолжительность фильма должна быть положительной", exception.getMessage());
    }

    @Test
    void testFilmNameValidation() {
        final Film film = new Film();
        Exception exception = assertThrows(ValidationException.class, () -> filmController.validateName(film));
        assertEquals("Название фильма пустое!", exception.getMessage());
    }

    @Test
    void testFilmDateValidation() {
        final Film film = new Film();
        film.setReleaseDate(LocalDate.parse("1895-12-27"));
        Exception exception = assertThrows(ValidationException.class, () -> filmController.validateReleaseDate(film));
        assertEquals("Дата релиза фильма должна быть не раньше 28 декабря 1895 года!", exception.getMessage());
    }

    @Test
    void testFilmDateValidationBorder() {
        final Film film = new Film();
        film.setReleaseDate(LocalDate.parse("1895-12-28"));
        assertTrue(filmController.validateReleaseDate(film));
    }

    @Test
    void testFilmCreation() {
        filmController.post(film1);
        filmController.post(film2);
        assertEquals(2, filmController.returnList().size());
    }

    @Test
    void testFilmUpdate() {
        filmController.post(film2);
        film2.setName("New Name");
        filmController.put(film2);
        assertEquals("New Name", filmController.returnList().get(film2.getId()).getName());
    }

    @Test
    void testFilmUpdateUnknownId() {
        filmController.post(film2);
        film2.setId(999);
        Exception exception = assertThrows(ValidationException.class, () -> filmController.put(film2));
        assertEquals("Фильм \"Name 2\" не найден в базе данных", exception.getMessage());
    }

    @Test
    void testFilmsGet() {
        filmController.post(film1);
        assertEquals(1, filmController.getAll().size());
        filmController.post(film2);
        assertEquals(2, filmController.getAll().size());
    }
}
