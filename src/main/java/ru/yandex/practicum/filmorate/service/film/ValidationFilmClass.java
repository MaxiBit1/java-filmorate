package ru.yandex.practicum.filmorate.service.film;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.ExceptionAndLogs;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.Month;

/**
 * класс валидации фильмов
 * @author Max Vasilyev
 * @version 1.0
 */
@Slf4j
public class ValidationFilmClass {
    /**
     * Метод для обработки валидации фильма
     *
     * @param film - фильм
     */
    public static void filmValidation(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            log.warn(ExceptionAndLogs.NAME_FILM.getDescription());
            throw new ValidationException(ExceptionAndLogs.NAME_FILM.getDescription());
        } else if (film.getDescription().length() > 200) {
            log.warn(ExceptionAndLogs.DESCRIPTION_FILM.getDescription());
            throw new ValidationException(ExceptionAndLogs.DESCRIPTION_FILM.getDescription());
        } else if (film.getReleaseDate().isBefore(LocalDate.of(1895, Month.DECEMBER, 27))) {
            log.warn(ExceptionAndLogs.DATE_FILM.getDescription());
            throw new ValidationException(ExceptionAndLogs.DATE_FILM.getDescription());
        } else if (film.getDuration() <= 0) {
            log.warn(ExceptionAndLogs.LENGTH_FILM.getDescription());
            throw new ValidationException(ExceptionAndLogs.LENGTH_FILM.getDescription());
        }
    }
}
