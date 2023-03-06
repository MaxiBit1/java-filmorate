package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ExceptionAndLogs;
import ru.yandex.practicum.filmorate.exception.FilmException;
import ru.yandex.practicum.filmorate.model.Genres;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * класс-сервис для жанров
 * @author Max Vailyev
 * @version 1.0
 */
@Service
@Slf4j
public class GenreService {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GenreService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * получение жанр по айди
     * @param id - айди жанра
     * @return - жанр
     */
    public Genres getGenreById(int id) {
        if(!checkGenre(id)) {
            log.warn(ExceptionAndLogs.ID_ERROR.getDescription());
            throw new FilmException(ExceptionAndLogs.ID_ERROR.getDescription());
        }
        String sqlRequest = "SELECT * FROM genre WHERE id_genre = ?";
        return jdbcTemplate.query(sqlRequest, (rs, rowNum) -> makeGenre(rs), id).stream().findFirst().get();
    }

    /**
     * получение списка всех жанров
     * @return - список жанров
     */
    public List<Genres> getGenres() {
        String sqlRequest = "SELECT * FROM genre";
        return jdbcTemplate.query(sqlRequest, (rs, rowNum) -> makeGenre(rs));
    }

    /**
     * проверка айди на нахождении в тадлице
     * @param id - айди
     * @return - результат проверки
     */
    private boolean checkGenre(int id) {
        for (Genres genre : getGenres()) {
            if(genre.getId() == id)
                return true;
        }
        return false;
    }

    private Genres makeGenre(ResultSet rs) throws SQLException {
        return new Genres(rs.getInt("id_genre"), rs.getString("name_genre"));
    }
}
