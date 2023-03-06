package ru.yandex.practicum.filmorate.service.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ExceptionAndLogs;
import ru.yandex.practicum.filmorate.exception.FilmException;
import ru.yandex.practicum.filmorate.exception.UserException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Класс бизнес-логики по фильмам
 *
 * @author Max Vasilyev
 * @version 1.0
 */
@Slf4j
@Service("InMemoryFilmService")
public class ImMemoryFilmService implements FilmService{

    private FilmStorage filmStorage;
    private UserStorage userStorage;

    @Autowired
    public ImMemoryFilmService(@Qualifier("InMemoryFilmStorage") FilmStorage filmStorage, @Qualifier("InMemoryUserStorage") UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    @Override
    public Film getFilmById(long idFilm) {
        if (idFilm < 0 || idFilm > filmStorage.getListFilm().size()) {
            log.warn(ExceptionAndLogs.ID_ERROR.getDescription());
            throw new FilmException(ExceptionAndLogs.ID_ERROR.getDescription());
        }
        log.info("Получение фильма по id");
        return filmStorage.getListFilm().get((int) (idFilm - 1));
    }

    @Override
    public void setLikeFilm(long idFilm, long idUser) {
        if (idFilm < 0 || idFilm > filmStorage.getListFilm().size() ) {
            log.warn(ExceptionAndLogs.ID_ERROR.getDescription());
            throw new FilmException(ExceptionAndLogs.ID_ERROR.getDescription());
        }
        if (idUser < 0 || idUser > userStorage.getUsers().size()) {
            log.warn(ExceptionAndLogs.ID_ERROR.getDescription());
            throw new UserException(ExceptionAndLogs.ID_ERROR.getDescription());
        }
        log.info("Лайк фильму " + idFilm + " поставлен");
        userStorage.getUser().get(idUser).getLikesFilmsId().add(idFilm);
    }

    @Override
    public void deleteLikeFilm(long idFilm, long idUser) {
        if (idFilm < 0 || idFilm > filmStorage.getListFilm().size()) {
            log.warn(ExceptionAndLogs.ID_ERROR.getDescription());
            throw new FilmException(ExceptionAndLogs.ID_ERROR.getDescription());
        }
        if (idUser < 0 || idUser > userStorage.getUsers().size()) {
            log.warn(ExceptionAndLogs.ID_ERROR.getDescription());
            throw new UserException(ExceptionAndLogs.ID_ERROR.getDescription());
        }
        log.info("Лайк фильму " + idFilm + " удален");
        userStorage.getUser().get(idUser).getLikesFilmsId().remove(idFilm);
    }

    @Override
    public List<Film> getPopularFilms(int count) {
        log.info("Список популярных фильмов получен");
        return filmStorage.getListFilm().stream()
                .limit(count)
                .collect(Collectors.toList());
    }
}
