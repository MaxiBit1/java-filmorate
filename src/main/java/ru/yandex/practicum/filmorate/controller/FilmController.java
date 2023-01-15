package ru.yandex.practicum.filmorate.controller;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.FilmException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Класс контроллера для фильмов
 *
 * @author Max Vasilyev
 * @version 1.0
 */
@RestController
public class FilmController {

    @Getter
    private Map<Integer, Film> mapFilms = new HashMap<>();
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);

    /**
     * Метод получения списка фильмов
     *
     * @return список фильмов
     */
    @GetMapping("/films")
    public List<Film> getMap() {
        return new ArrayList<>(mapFilms.values());
    }

    /**
     * Метод создания фильма
     *
     * @param film - фильм
     * @return сохраненный фильм
     */
    @PostMapping("/films")
    public Film createFilm(@RequestBody Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            log.warn("Ошибка в названии фильма!");
            throw new ValidationException("Ошибка в названии фильма!");
        } else if (film.getDescription().length() > 200) {
            log.warn("Ошибка в длине фильма");
            throw new ValidationException("Ошибка в длине фильма");
        } else if (film.getReleaseDate().isBefore(LocalDate.of(1895, Month.DECEMBER, 27))) {
            log.warn("Ошибка в дате релиза фильма");
            throw new ValidationException("Ошибка в дате релиза фильма");
        } else if (film.getDuration() <= 0) {
            log.warn("Ошибка в продолжительности фильма");
            throw new ValidationException("Ошибка в продолжительности фильма");
        } else {
            film.setId(mapFilms.size() + 1);
            mapFilms.put(film.getId(), film);
            log.info("Фильм создан");
            return film;
        }
    }

    /**
     * Метод обновления фильма
     *
     * @param film - фильм
     * @return обновленный фильм
     */
    @PutMapping("/films")
    public Film updateFilm(@RequestBody Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            log.warn("Ошибка в названии фильма!");
            throw new ValidationException("Ошибка в названии фильма!");
        } else if (film.getDescription().length() > 200) {
            log.warn("Ошибка в длине фильма");
            throw new ValidationException("Ошибка в длине фильма");
        } else if (film.getReleaseDate().isBefore(LocalDate.of(1895, Month.DECEMBER, 28))) {
            log.warn("Ошибка в дате релиза фильма");
            throw new ValidationException("Ошибка в дате релиза фильма");
        } else if (film.getDuration() <= 0) {
            log.warn("Ошибка в продолжительности фильма");
            throw new ValidationException("Ошибка в продолжительности фильма");
        } else if (!mapFilms.containsKey(film.getId())) {
            log.warn("Такого фильма нет");
            throw new FilmException("Такого фильма нет");
        } else {
            Film filmFromList = mapFilms.get(film.getId());
            setAtributeFilm(film, filmFromList);
            log.info("Фильм обновлен");
            return filmFromList;
        }
    }

    /**
     * Установка атбрибутов для фильма
     *
     * @param film         - обновленный фильм
     * @param filmFromList - фильм из хранилища
     */
    private void setAtributeFilm(Film film, Film filmFromList) {
        filmFromList.setName(film.getName());
        filmFromList.setDescription(film.getDescription());
        filmFromList.setReleaseDate(film.getReleaseDate());
        filmFromList.setDuration(film.getDuration());
    }
}
