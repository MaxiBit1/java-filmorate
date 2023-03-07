package ru.yandex.practicum.filmorate.service.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmService {
    /**
     * Метод получения фильма по айди
     *
     * @param idFilm - айди фильма
     * @return - фильм
     */
    Film getFilmById(long idFilm);
    /**
     * Метод установки лайка за фильм
     *
     * @param idFilm - айди фильма
     * @param idUser - айди юзера
     */
    void setLikeFilm(long idFilm, long idUser);
    /**
     * Метод удаления лайка за фильм
     *
     * @param idFilm - айди фильма
     * @param idUser - айди юзера
     */
    void deleteLikeFilm(long idFilm, long idUser);
    /**
     * Метод получения всех фильмов
     *
     * @param count - лимит на фильмы
     * @return - список популярных фильмов
     */
    List<Film> getPopularFilms(int count);
}
