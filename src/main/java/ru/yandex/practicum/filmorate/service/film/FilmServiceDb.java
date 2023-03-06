package ru.yandex.practicum.filmorate.service.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ExceptionAndLogs;
import ru.yandex.practicum.filmorate.exception.FilmException;
import ru.yandex.practicum.filmorate.exception.UserException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genres;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.util.List;

/**
 * Класс-сервис для работы с БД
 * @author Max Vasilyev
 * @version 1.0
 */
@Service("FilmServiceDb")
@Slf4j
public class FilmServiceDb implements FilmService{

    private final JdbcTemplate jdbcTemplate;
    private FilmDbStorage filmDbStorage;
    private UserDbStorage userDbStorage;

    @Autowired
    public FilmServiceDb(JdbcTemplate jdbcTemplate, FilmDbStorage filmDbStorage, UserDbStorage userDbStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.filmDbStorage = filmDbStorage;
        this.userDbStorage = userDbStorage;
    }


    @Override
    public Film getFilmById(long idFilm) {
        if(!filmDbStorage.checkIdFilm(idFilm)) {
            log.warn(ExceptionAndLogs.ID_ERROR.getDescription());
            throw new FilmException(ExceptionAndLogs.ID_ERROR.getDescription());
        }
        String sqlRequest = "SELECT fi.id_film,\n" +
                "       fi.name_film,\n" +
                "       fi.description,\n" +
                "       fi.release_date,\n" +
                "       fi.duration,\n" +
                "       fi.id_mpa,\n" +
                "       m.id_mpa,\n" +
                "       m.name_mpa \n" +
                "FROM films AS fi\n" +
                "INNER JOIN mpa AS m on m.id_mpa = fi.id_mpa\n " +
                "WHERE id_film = ?";
        List<Film> films = jdbcTemplate.query(sqlRequest, (rs, rowNum) -> filmDbStorage.makeFilm(rs), idFilm);
        SqlRowSet sqlRowSetGenres = jdbcTemplate.queryForRowSet("SELECT f.id_film,\n" +
                "       g.id_genre,\n" +
                "       g.name_genre\n" +
                "FROM films AS f\n" +
                "INNER JOIN genre_films AS gf on f.id_film = gf.id_film\n" +
                "INNER JOIN GENRE AS g on gf.id_genre = g.id_genre\n"+
                "WHERE f.ID_FILM = ?", idFilm);
        while (sqlRowSetGenres.next()) {
            for (Film film : films) {
                if(film.getId() == sqlRowSetGenres.getLong("id_film")) {
                    film.getGenres().add(new Genres(sqlRowSetGenres.getInt("id_genre"),
                            sqlRowSetGenres.getString("name_genre")));
                }
            }
        }
        return films.stream().findFirst().get();
    }

    @Override
    public void setLikeFilm(long idFilm, long idUser) {
        if(!filmDbStorage.checkIdFilm(idFilm)) {
            log.warn(ExceptionAndLogs.ID_ERROR.getDescription());
            throw new FilmException(ExceptionAndLogs.ID_ERROR.getDescription());
        }
        if(!userDbStorage.checkUserId(idUser)) {
            log.warn(ExceptionAndLogs.ID_ERROR.getDescription());
            throw new UserException(ExceptionAndLogs.ID_ERROR.getDescription());
        }
        String sqlRequest = "INSERT INTO likes_films (id_films, id_user)\n" +
                "VALUES (?,?)";
        jdbcTemplate.update(sqlRequest, idFilm, idUser);
    }

    @Override
    public void deleteLikeFilm(long idFilm, long idUser) {
        if(!checkFilmInLikeFilm(idFilm) || !filmDbStorage.checkIdFilm(idFilm)) {
            log.warn(ExceptionAndLogs.ID_ERROR.getDescription());
            throw new FilmException(ExceptionAndLogs.ID_ERROR.getDescription());
        }
        if(!checkUserInLikeFilm(idUser) || !userDbStorage.checkUserId(idUser)) {
            log.warn(ExceptionAndLogs.ID_ERROR.getDescription());
            throw new UserException(ExceptionAndLogs.ID_ERROR.getDescription());
        }
        String sqlRequest = "DELETE FROM likes_films WHERE id_films = ? AND id_user = ?";
        jdbcTemplate.update(sqlRequest, idFilm, idUser);
    }

    @Override
    public List<Film> getPopularFilms(int count) {
        String sqlRequest = "SELECT fi.id_film,\n" +
                "       fi.name_film,\n" +
                "       fi.description,\n" +
                "       fi.release_date,\n" +
                "       fi.duration,\n"+
                "       fi.id_mpa,\n" +
                "       m.name_mpa\n" +
                "FROM films AS fi\n" +
                "INNER JOIN likes_films AS lf on fi.id_film = lf.id_films\n" +
                "INNER JOIN mpa AS m on fi.id_mpa = m.id_mpa\n" +
                "GROUP BY fi.id_film\n" +
                "ORDER BY COUNT(lf.id_films) DESC\n" +
                "LIMIT ?";
        List<Film> films = jdbcTemplate.query(sqlRequest, (rs, rowNum) -> filmDbStorage.makeFilm(rs), count);
        filmDbStorage.setGenreToFilms(films);
        if(films.isEmpty()) {
            films = filmDbStorage.getListFilm();
        }
        return films;
    }

    /**
     * Проверка фильма в таблице лайков
     * @param idFilm - айди фильма
     * @return - результат проверки
     */
    private boolean checkFilmInLikeFilm(long idFilm) {
        String sqlRequest = "SELECT fi.id_film,\n" +
                "       fi.name_film,\n" +
                "       fi.description,\n" +
                "       fi.release_date,\n" +
                "       fi.duration,\n"+
                "       fi.id_mpa,\n" +
                "       m.name_mpa\n" +
                "FROM films AS fi\n" +
                "INNER JOIN likes_films AS lf on fi.id_film = lf.id_films\n" +
                "INNER JOIN mpa AS m on fi.id_mpa = m.id_mpa\n" +
                "GROUP BY lf.id_films";
        List<Film> films = jdbcTemplate.query(sqlRequest, (rs, rowNum) -> filmDbStorage.makeFilm(rs));
        for (Film film : films) {
            if(film.getId() == idFilm) {
                return true;
            }
        }
        return false;
    }

    /**
     * проверка юзера в таблице лайков
     * @param idUser - айди юзера
     * @return - результат проверки
     */
    private boolean checkUserInLikeFilm(long idUser) {
        String sqlRequest = "SELECT us.id_user,\n" +
                "       us.email,\n" +
                "       us.login,\n" +
                "       us.name,\n" +
                "       us.birthday\n" +
                "FROM users AS us\n" +
                "INNER JOIN likes_films AS lf on us.id_user = lf.id_user\n" +
                "GROUP BY lf.id_user";
        List<User> users = jdbcTemplate.query(sqlRequest, (rs, rowNum) -> userDbStorage.makeUser(rs));
        for (User user : users) {
            if(user.getId() == idUser){
                return true;
            }
        }
        return false;
    }

    /**
     * проверка на принадлежность фильма в таблице фильмов
     * @param idFilm - айди фильма
     * @return - результат проверки
     */
    private boolean checkIdInFilms(long idFilm) {
        for (Film film : filmDbStorage.getListFilm()) {
            if(film.getId() == idFilm) {
                return true;
            }
        }
        return false;
    }

    /**
     * проверка на принадлежность юзера в таблице юзеров
     * @param idUser - айди юзера
     * @return - результат проверки
     */
    private boolean checkIdInUsers(long idUser) {
        for (User user : userDbStorage.getUsers()) {
            if(user.getId() == idUser){
                return true;
            }
        }
        return false;
    }
}
