package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.models.Genre;
import ru.yandex.practicum.filmorate.models.MPA;
import ru.yandex.practicum.filmorate.storage.interfaces.GenreMpaStorage;
import ru.yandex.practicum.filmorate.validationException.NotFoundException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Primary
@Slf4j
@Component
@RequiredArgsConstructor
public class GenreMpaDbStorage implements GenreMpaStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Genre> getListGenre() {
        String sqlQuery = "select * from genre";
        return jdbcTemplate.query(sqlQuery, this::rowGenre);
    }

    @Override
    public Genre getGenreById(int id) {
        String sqlQuery = "select * from genre where genre_id = ?";
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(sqlQuery, id);
        if (userRows.next()) {
            log.info("Найден жанр с идентификатором: {}", userRows.getString("genre_id"));
            return jdbcTemplate.queryForObject(sqlQuery, this::rowGenre, id);
        } else {
            log.warn("Жанр с идентификатором {} не найден.", id);
            throw new NotFoundException("Такого жанра не существует");
        }
    }

    @Override
    public List<MPA> getListMpa() {
        String sqlQuery = "select * from MPA";
        return jdbcTemplate.query(sqlQuery, this::rowMpa);
    }

    @Override
    public MPA getMpaById(int id) {
        String sql = "select * from MPA where MPA_id = ?";
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(sql, id);
        if (userRows.next()) {
            log.info("Найден рейтинг с идентификатором: {}", userRows.getString("MPA_id"));
            return jdbcTemplate.queryForObject(sql, this::rowMpa, id);
        } else {
            log.warn("Жанр с рейтингом {} не найден.", id);
            throw new NotFoundException("Такого рейтинга не существует");
        }
    }

    private Genre rowGenre(ResultSet rs, int i) throws SQLException {
        return Genre.builder()
                .id(rs.getInt("genre_id"))
                .name(rs.getString("name"))
                .build();
    }

    private MPA rowMpa(ResultSet rs, int i) throws SQLException {
        return MPA.builder()
                .id(rs.getInt("MPA_id"))
                .name(rs.getString("MPA_name"))
                .build();
    }
}
