package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Map;

/**
 * Интерфейс хранения фильмов
 *
 * @author Max Vailyev
 * @version 1.0
 */
public interface FilmStorage {
    /**
     * Метод получения списка фильмов
     *
     * @return список фильмов
     */
    List<Film> getListFilm();

    /**
     * Метод создания фильма
     *
     * @param film - фильм
     * @return сохраненный фильм
     */
    Film createFilm(Film film);

    /**
     * Метод обновления фильма
     *
     * @param film - фильм
     * @return обновленный фильм
     */
    Film updateFilm(Film film);

    Map<Long, Film> getFilms();
}
