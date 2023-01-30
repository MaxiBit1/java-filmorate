package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
@Service
public class FilmService {

    private FilmStorage filmStorage;
    private UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    /**
     * Метод получения фильма по айди
     *
     * @param idFilm - айди фильма
     * @return - фильм
     */
    public Film getFilmById(long idFilm) {
        if (idFilm < 0 || idFilm > filmStorage.getListFilm().size()) {
            log.warn("Ошибка в id");
            throw new FilmException("Id меньше или равен, больше длины списка");
        }
        log.info("Получение фильма по id");
        return filmStorage.getListFilm().get((int) (idFilm - 1));
    }

    /**
     * Метод установки лайка за фильм
     *
     * @param idFilm - айди фильма
     * @param idUser - айди юзера
     */
    public void setLikeFilm(long idFilm, long idUser) {
        if (idFilm < 0 || idFilm > filmStorage.getListFilm().size()) {
            log.warn("Ошибка в id");
            throw new FilmException("Id меньше или равен, больше длины списка");
        }
        if (idUser < 0 || idUser > userStorage.getUsers().size()) {
            log.warn("Ошибка в id");
            throw new UserException("Id меньше нуля или равен, больше длине списка юзеров");
        }
        log.info("Лайк фильму " + idFilm + " поставлен");
        filmStorage.getFilms().get(idFilm).getLikes().add(idUser);
        userStorage.getUser().get(idUser).getLikesFilmsId().add(idFilm);
    }

    /**
     * Метод удаления лайка за фильм
     *
     * @param idFilm - айди фильма
     * @param idUser - айди юзера
     */
    public void deleteLikeFilm(long idFilm, long idUser) {
        if (idFilm < 0 || idFilm > filmStorage.getListFilm().size()) {
            log.warn("Ошибка в id");
            throw new FilmException("Id меньше или равен, больше длины списка");
        }
        if (idUser < 0 || idUser > userStorage.getUsers().size()) {
            log.warn("Ошибка в id");
            throw new UserException("Id меньше нуля или равен, больше длине списка юзеров");
        }
        log.info("Лайк фильму " + idFilm + " удален");
        filmStorage.getFilms().get(idFilm).getLikes().remove(idUser);
        userStorage.getUser().get(idUser).getLikesFilmsId().remove(idFilm);
    }

    /**
     * Метод получения всех фильмов
     *
     * @param count - лимит на фильмы
     * @return - список популярных фильмов
     */
    public List<Film> getPopularFilms(int count) {
        log.info("Список популярных фильмов получен");
        return filmStorage.getListFilm().stream()
                .sorted(((o1, o2) -> o2.getLikes().size() - o1.getLikes().size()))
                .limit(count)
                .collect(Collectors.toList());
    }
}
