package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.FilmService;
import ru.yandex.practicum.filmorate.service.film.ImMemoryFilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.List;

/**
 * Класс контроллера для фильмов
 *
 * @author Max Vasilyev
 * @version 1.0
 */
@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {

    private FilmStorage filmStorage;
    private FilmService filmService;

    @Autowired
    public FilmController(@Qualifier("FilmDbStorage") FilmStorage filmStorage, @Qualifier("FilmServiceDb") FilmService filmService) {
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
        log.info("Get метод получения списка фильмов");
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
        log.info("Post метод создания фильма");
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
        log.info("Put метод обновления фильма");
        return filmStorage.updateFilm(film);
    }

    /**
     * Метод получения фильма по айди
     *
     * @param id - айди фильма
     * @return - фильм
     */
    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable long id) {
        log.info("Get метод на получения фильма по id - " + id);
        return filmService.getFilmById(id);
    }

    /**
     * Метод установки лайка за фильм
     *
     * @param id     - айди фильма
     * @param userId - айди юзера
     */
    @PutMapping("/{id}/like/{userId}")
    public void setLikeFilm(@PathVariable long id, @PathVariable long userId) {
        log.info("Put установки лайка фильму - " + id + " юзером - " + userId);
        filmService.setLikeFilm(id, userId);
    }

    /**
     * Метод удаления фильма
     *
     * @param id     - айди фильма
     * @param userId - айди юзера
     */
    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLikeFilm(@PathVariable long id, @PathVariable long userId) {
        log.info("Delete метод удаления лайка с фильма - " + id + " юзером - "+ userId);
        filmService.deleteLikeFilm(id, userId);
    }

    /**
     * Список популярных фильмов
     *
     * @param count - до какого фильма
     * @return - список популярных фильмов
     */
    @GetMapping("/popular")
    public List<Film> popularFilms(@RequestParam(defaultValue = "10") String count) {
        log.info("Get списка первых " + count + " популярных фильмовc");
        return filmService.getPopularFilms(Integer.parseInt(count));
    }
}
