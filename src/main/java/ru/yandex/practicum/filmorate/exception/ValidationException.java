package ru.yandex.practicum.filmorate.exception;

/**
 * Класс для исключения по валидации
 *
 * @author Max Vasilyev
 * @version 1.0
 */
public class ValidationException extends RuntimeException {

    public ValidationException(String textError) {
        super(textError);
    }
}
