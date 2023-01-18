package ru.yandex.practicum.filmorate.exception;

/**
 * Класс для исключения по фильмам
 *
 * @author Max Vasilyev
 * @version 1.0
 */
public class FilmException extends RuntimeException {

    public FilmException(String text) {
        super(text);
    }

}
