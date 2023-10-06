package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controllers.UserController;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.models.Film;
import ru.yandex.practicum.filmorate.controllers.FilmController;
import ru.yandex.practicum.filmorate.models.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FilmorateApplicationTests {

	private FilmController filmController;
	private UserController userController;
	private Film film1;
	private Film film2;
	private User user1;
	private User user2;

	@BeforeEach
	void init() {
		filmController = new FilmController();
		userController = new UserController();

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

		user1 = new User();
		user1.setId(1);
		user1.setName("Name 1");
		user1.setBirthday(LocalDate.parse("1980-10-10"));
		user1.setLogin("Login 1");
		user1.setEmail("me@home.com");

		user2 = new User();
		user2.setId(2);
		user2.setName("Name 2");
		user2.setBirthday(LocalDate.parse("1981-10-10"));
		user2.setLogin("Login 2");
		user2.setEmail("haha@haha.com");
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
		assertEquals(2, filmController.getHashMap().size());
	}

	@Test
	void testFilmUpdate() {
		filmController.post(film2);
		film2.setName("New Name");
		filmController.put(film2);
		assertEquals("New Name", filmController.getHashMap().get(film2.getId()).getName());
	}

	@Test
	void testFilmUpdateUnknownId() {
		filmController.post(film2);
		film2.setId(999);
		Exception exception = assertThrows(ValidationException.class, () -> filmController.put(film2));
		assertEquals("Фильм не найден в базе данных", exception.getMessage());
	}

	@Test
	void testFilmsGet() {
		filmController.post(film1);
		assertEquals(1, filmController.get().size());
		filmController.post(film2);
		assertEquals(2, filmController.get().size());
	}

	@Test
	void testUserEmailValidation() {
		user1.setEmail("meme.com");
		Exception exception = assertThrows(ValidationException.class, () -> userController.validateEmail(user1));
		assertEquals("Указана неверная электронная почта пользователя!", exception.getMessage());
	}

	@Test
	void testUserLoginValidation() {
		user1.setLogin("");
		Exception exception = assertThrows(ValidationException.class, () -> userController.validateLogin(user1));
		assertEquals("Указан неверный логин пользователя!", exception.getMessage());
	}

	@Test
	void testUserNameValidation() {
		user1.setName("");
		User testUser = userController.validateName(user1);
		assertEquals(user1.getLogin(), testUser.getName());
	}

	@Test
	void testUserBirthdayValidation() {
		user1.setBirthday(LocalDate.parse("2025-10-10"));
		Exception exception = assertThrows(ValidationException.class, () -> userController.validateBirthday(user1));
		assertEquals("Дата рождения пользователя не может быть в будущем!", exception.getMessage());
	}

	@Test
	void testUserCreation(){
		userController.post(user1);
		assertEquals(1, userController.getHashMap().size());
		userController.post(user2);
		assertEquals(2, userController.getHashMap().size());
	}

	@Test
	void testUserUpdate(){
		userController.post(user1);
		user1.setName("New Name");
		userController.put(user1);
		assertEquals("New Name", userController.getHashMap().get(user1.getId()).getName());
	}

	@Test
	void testUserUpdateUnknownId(){
		userController.post(user1);
		user1.setId(999);
		Exception exception = assertThrows(ValidationException.class, () -> userController.put(user1));
		assertEquals("Пользователь не найден в базе данных", exception.getMessage());
	}

	@Test
	void testUsersGet(){
		userController.post(user1);
		assertEquals(1, userController.get().size());
		userController.post(user2);
		assertEquals(2, userController.get().size());
	}
}

