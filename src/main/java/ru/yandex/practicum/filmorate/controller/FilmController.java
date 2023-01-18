package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
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
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private final Map<Integer, Film> films = new HashMap<>();

    /**
     * Метод получения списка фильмов
     *
     * @return список фильмов
     */
    @GetMapping
    public List<Film> getListFilm() {
        return new ArrayList<>(films.values());
    }

    /**
     * Метод создания фильма
     *
     * @param film - фильм
     * @return сохраненный фильм
     */
    @PostMapping
    public Film createFilm(@RequestBody Film film) {
        filmValidation(film);
        if(films.containsKey(film.getId())) {
            log.warn("Фильм уже есть");
            throw new FilmException("Фильм уже есть");
        } else {
            film.setId(films.size() + 1);
            films.put(film.getId(), film);
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
    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        filmValidation(film);
        if(!films.containsKey(film.getId())) {
            log.warn("Фильма нет");
            throw new FilmException("Фильма нет");
        } else {
            Film filmFromList = films.get(film.getId());
            setAtributeFilm(film, filmFromList);
            log.info("Фильм обновлен");
            return filmFromList;
        }
    }

    /**
     * Метод для обработки валидации фильма
     *
     * @param film - фильм
     */
    private static void filmValidation(Film film) {
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
