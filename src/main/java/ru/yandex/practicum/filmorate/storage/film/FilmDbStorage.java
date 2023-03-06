package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ExceptionAndLogs;
import ru.yandex.practicum.filmorate.exception.FilmException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genres;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.film.ValidationFilmClass;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Класс для работы с бд фильмов
 * @author Max Vailyev
 * @version 1.0
 */
@Component("FilmDbStorage")
@Slf4j
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;


    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Film> getListFilm() {
        String sqlRequest = "SELECT f.id_film,\n" +
                "       f.name_film,\n" +
                "       f.description,\n" +
                "       f.release_date,\n" +
                "       f.duration,\n" +
                "       f.id_mpa,\n" +
                "       mp.name_mpa\n" +
                "FROM films AS f\n" +
                "INNER JOIN mpa AS mp on mp.id_mpa = f.id_mpa";
        List<Film> resultFilmList = jdbcTemplate.query(sqlRequest, (rs, rowNum) -> makeFilm(rs));
        setGenreToFilms(resultFilmList);
        return resultFilmList;
    }

    @Override
    public Film createFilm(Film film) {
        List<Genres> newListGenre = new ArrayList<>();
        ValidationFilmClass.filmValidation(film);
        String sqlRequest = "INSERT INTO films (name_film, description, release_date, duration, id_mpa)" +
                "VALUES(?,?,?,?,?)";
        jdbcTemplate.update(sqlRequest,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId());
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet("SELECT ID_FILM FROM FILMS WHERE NAME_FILM = ?", film.getName());
        if (sqlRowSet.next()) {
            film.setId(sqlRowSet.getLong("id_film"));
        }
        if (film.getGenres() != null) {
            newListGenre = setGenres(film);
        }
        return new Film(film.getId(),
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa(),
                newListGenre);
    }

    @Override
    public Film updateFilm(Film film) {
        if(!checkIdFilm(film.getId())) {
            log.warn(ExceptionAndLogs.NO_FILM.getDescription());
            throw new FilmException(ExceptionAndLogs.NO_FILM.getDescription());
        }
        List<Genres> newListGenre = new ArrayList<>();
        ValidationFilmClass.filmValidation(film);
        String sqlRequest = "UPDATE films SET " +
                "name_film = ?, description = ?, release_date = ?, duration = ?, id_mpa = ? " +
                "WHERE id_film = ?";
        jdbcTemplate.update(sqlRequest,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());
        String sqlRequestForGenreDelete = "DELETE FROM GENRE_FILMS WHERE ID_FILM = ?";
        jdbcTemplate.update(sqlRequestForGenreDelete, film.getId());
        if (film.getGenres() != null) {
            newListGenre = setGenres(film);
        }
        return new Film(film.getId(),
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa(),
                newListGenre);
    }

    @Override
    public Map<Long, Film> getFilms() {
        return null;
    }

    /**
     * метод для сохдания фильма из строки sql
     * @param rs - срока
     * @return - фильм
     * @throws SQLException - исключения sql
     */
    public Film makeFilm(ResultSet rs) throws SQLException {
        long id = rs.getLong("id_film");
        String nameFilm = rs.getString("name_film");
        String description = rs.getString("description");
        LocalDate releaseDate = rs.getDate("release_date").toLocalDate();
        int duration = rs.getInt("duration");
        Mpa mpa = new Mpa(rs.getInt("id_mpa"), rs.getString("name_mpa"));
        return new Film(id, nameFilm, description, releaseDate, duration, mpa, new ArrayList<>());
    }

    /**
     * установка жанров для списков фильмов
     * @param films - список фильмов
     */
    public void setGenreToFilms(List<Film> films) {
        SqlRowSet sqlRowSetGenres = jdbcTemplate.queryForRowSet("SELECT f.id_film,\n" +
                "       g.id_genre,\n" +
                "       g.name_genre\n" +
                "FROM films AS f\n" +
                "INNER JOIN genre_films AS gf on f.id_film = gf.id_film\n" +
                "INNER JOIN GENRE AS g on gf.id_genre = g.id_genre");
        while (sqlRowSetGenres.next()) {
            for (Film film : films) {
                if (film.getId() == sqlRowSetGenres.getLong("id_film")) {
                    film.getGenres().add(new Genres(sqlRowSetGenres.getInt("id_genre"),
                            sqlRowSetGenres.getString("name_genre")));
                }
            }
        }
    }

    /**
     * проверка принадлежности жанра в таблице genre_film
     * @param idFilm - айди фильма
     * @param idGenres - айди жанра
     * @return - результат проверки
     */
    private boolean checkGenreIDTable(long idFilm, int idGenres) {
        List<Integer> filmsIds = new ArrayList<>();
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet("SELECT ID_GENRE FROM GENRE_FILMS WHERE ID_FILM = ?", idFilm);
        while (sqlRowSet.next()) {
            filmsIds.add(sqlRowSet.getInt("id_genre"));
        }
        for (Integer filmsId : filmsIds) {
            if (filmsId == idGenres) {
                return false;
            }
        }
        return true;
    }

    /**
     * проверка на принадлежность фильма в таблице фильмов
     * @param idFilm - айди фильма
     * @return - результат проверки
     */
    public boolean checkIdFilm(long idFilm) {
        List<Integer> films = jdbcTemplate.query("SELECT id_film FROM films WHERE id_film = ?",
                (rs, rowNum) -> rs.getInt("id_film"), idFilm);
        if(films.isEmpty()) {
            return false;
        }
        return true;
    }

    /**
     * установка списка жанров фильму
     * @param film - фильм
     * @return - список жанров
     */
    private List<Genres> setGenres(Film film) {
        List<Genres> newListGenre = new ArrayList<>();
        String sqlRequestForGenreCreate = "INSERT INTO GENRE_FILMS (ID_FILM, ID_GENRE) VALUES ( ?,? )";
        for (Genres genre : film.getGenres()) {
            if (checkGenreIDTable(film.getId(), genre.getId())) {
                jdbcTemplate.update(sqlRequestForGenreCreate,
                        film.getId(),
                        genre.getId());
                newListGenre.add(genre);
            }
        }
        return newListGenre;
    }
}
