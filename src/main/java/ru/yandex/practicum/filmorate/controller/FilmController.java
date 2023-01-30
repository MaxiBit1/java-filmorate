package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.List;

/**
 * Класс контроллера для фильмов
 *
 * @author Max Vasilyev
 * @version 1.0
 */
@RestController
@RequestMapping("/films")
public class FilmController {

    private FilmStorage filmStorage;
    private FilmService filmService;

    @Autowired
    public FilmController(FilmStorage filmStorage, FilmService filmService) {
        this.filmStorage = filmStorage;
        this.filmService = filmService;
    }

    /**
     * Метод получения списка фильмов
     *
     * @return список фильмов
     */
    @GetMapping
    public List<Film> getListFilm() {
        return filmStorage.getListFilm();
    }

    /**
     * Метод создания фильма
     *
     * @param film - фильм
     * @return сохраненный фильм
     */
    @PostMapping
    public Film createFilm(@RequestBody Film film) {
        return filmStorage.createFilm(film);
    }

    /**
     * Метод обновления фильма
     *
     * @param film - фильм
     * @return обновленный фильм
         */
    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        return filmStorage.updateFilm(film);
    }

    /**
     * Метод получения фильма по айди
     * @param id - айди фильма
     * @return - фильм
     */
    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable long id) {
        return filmService.getFilmById(id);
    }

    /**
     * Метод установки лайка за фильм
     * @param id - айди фильма
     * @param userId - айди юзера
     */
    @PutMapping("/{id}/like/{userId}")
    public void setLikeUser(@PathVariable long id, @PathVariable long userId) {
        filmService.setLikeFilm(id, userId);
    }

    /**
     * Метод удаления фильма
     * @param id - айди фильма
     * @param userId - айди юзера
     */
    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLikeUser(@PathVariable long id, @PathVariable long userId) {
        filmService.deleteLikeFilm(id, userId);
    }

    /**
     * Список популярных фильмов
     * @param count - до какого фильма
     * @return - список популярных фильмов
     */
    @GetMapping("/popular")
    public List<Film> popularFilms(@RequestParam(defaultValue = "10") String count) {
        return filmService.getPopularFilms(Integer.parseInt(count));
    }
}
