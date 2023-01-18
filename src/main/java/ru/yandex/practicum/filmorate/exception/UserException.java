package ru.yandex.practicum.filmorate.exception;

/**
 * Класс для исключения по user`ам
 *
 * @author Max Vasilyev
 * @version 1.0
 */
public class UserException extends RuntimeException {

    public UserException(String text) {
        super(text);
    }
}
