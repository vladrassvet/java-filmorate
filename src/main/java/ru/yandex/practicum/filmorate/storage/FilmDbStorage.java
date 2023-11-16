package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.models.Film;
import ru.yandex.practicum.filmorate.models.Genre;
import ru.yandex.practicum.filmorate.models.MPA;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;
import ru.yandex.practicum.filmorate.validationException.ChangeException;
import ru.yandex.practicum.filmorate.validationException.NotFoundException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.lang.Long.compare;

@Primary
@Component
@Slf4j
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Film> getFilmMap() {
        String sqlQuery = "select * from films f join MPA m ON f.mpa=m.MPA_id";
        return jdbcTemplate.query(sqlQuery, this::mapRowToFilm);
    }

    @Override
    public Film createFilm(Film film) {
        String sqlQuery = "insert into films (name, description, release_data, duration, mpa)"
                + "values  (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
                    PreparedStatement prp = connection.prepareStatement(sqlQuery, new String[]{"film_id"});
                    prp.setString(1, film.getName());
                    prp.setString(2, film.getDescription());
                    prp.setObject(3, film.getReleaseDate());
                    prp.setInt(4, film.getDuration());
                    prp.setInt(5, film.getMpa().getId());
                    return prp;
                }, keyHolder
        );
        int id = keyHolder.getKey().intValue();
        film.setId(id);
        String sql = "insert into film_genre (film_id, genre_id)" + "values (?, ?)";
        for (Genre genre : film.getGenres()) {
            jdbcTemplate.update(sql, film.getId(), genre.getId());
        }
        return film;
    }

    @Override
    public Film updateFilms(Film film) {
        String sqlQuery = "update films set name = ?, description = ?, release_data = ?, duration = ?, mpa = ?" +
                "where film_id = ?";
        jdbcTemplate.update(sqlQuery, film.getName(), film.getDescription(), film.getReleaseDate(),
                film.getDuration(), film.getMpa().getId(), film.getId());
        String sqlDelGenre = "delete from film_genre where film_id = ? ";
        jdbcTemplate.update(sqlDelGenre, film.getId());
        for (Genre genre : film.getGenres()) {
            String sqlPut = "insert into public.film_genre (film_id, genre_id)" + "values (?, ?)";
            jdbcTemplate.update(sqlPut, film.getId(), genre.getId());
        }
        return getFilmById(film.getId());
    }

    @Override
    public Film getFilmById(int id) {
        String sqlGetFilm = "select * from films f join MPA m ON f.mpa=m.MPA_id where film_id = ?";
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(sqlGetFilm, id);
        if (userRows.next()) {
            log.info("По запросу найден фильм {} {}",
                    userRows.getString("film_id"), userRows.getString("name"));
            return jdbcTemplate.queryForObject(sqlGetFilm, this::mapRowToFilm, id);
        } else {
            log.warn("Фильм с идентификатором {} не найден.", id);
            throw new NotFoundException("Такого фильма не существует");

        }
    }

    public void addLike(int id, int userId) {
        String sqlLike = "insert into likes (film_id, user_id)" + "values(?, ?)";
        jdbcTemplate.update(sqlLike, id, userId);
    }

    public void deleteLike(int id, int userId) {
        String sqlDelLike = "select user_id = ? from likes";
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(sqlDelLike, userId);
        if (userRows.next()) {
            log.info("Лайк пользователя {} фильму {} удален", userId, id);
            jdbcTemplate.update("delete from likes where film_id = ? and user_id = ?", id, userId);
        } else {
            log.warn("Пользователь с идентификатором {} не найден.", userId);
            throw new ChangeException("Такого пользователя не существует");
        }
    }

    public List<Film> topLikesFilms(int count) {
        List<Film> films = getFilmMap();
        if (films.size() < count) {
            count = films.size();
        }
        return films.stream()
                .sorted((p0, p1) -> {
                    int comp = compare(p0.getLike(), p1.getLike());
                    return -1 * comp;
                }).limit(count)
                .collect(Collectors.toList());
    }

    private Film mapRowToFilm(ResultSet resultSet, int i) throws SQLException {
        MPA mpa = rowMpa(resultSet, i);
        String sql = "select * from film_genre fg join genre g ON fg.genre_id = g.genre_id where film_id = ? ";
        List<Genre> genreList = jdbcTemplate.query(sql, (rs, i1) -> rowGenre(rs, i1), resultSet.getInt("film_id"));
        Set<Genre> genres = new HashSet<>(genreList);
        String sqlLikes = "select count(user_id) from likes where film_id = ?";
        List<Integer> likesList = jdbcTemplate.queryForList(sqlLikes, Integer.class, resultSet.getInt("film_id"));
        int likes = likesList.get(0);
        Film film = new Film(
                resultSet.getString("name"),
                resultSet.getString("description"),
                resultSet.getDate("release_data").toLocalDate(),
                resultSet.getInt("duration"),
                likes,
                mpa,
                genres
        );
        film.setId(resultSet.getInt("film_id"));
        return film;
    }

    private MPA rowMpa(ResultSet rs, int i) throws SQLException {
        return MPA.builder()
                .id(rs.getInt("MPA_id"))
                .name(rs.getString("MPA_name"))
                .build();
    }

    private Genre rowGenre(ResultSet rs, int i) throws SQLException {
        return Genre.builder()
                .id(rs.getInt("genre_id"))
                .name(rs.getString("name"))
                .build();
    }
}