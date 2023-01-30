package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exception.FilmException;
import ru.yandex.practicum.filmorate.exception.FriendsException;
import ru.yandex.practicum.filmorate.exception.UserException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.ErrorResponse;

/**
 * Класс для обратки и отправки ошибки клиенту
 *
 * @author Max Vasilyev
 * @version 1.0
 */
@RestControllerAdvice
public class ErrorHandler {

    /**
     * Метод ошибки валидации
     *
     * @param e - ошибка валидации
     * @return - ответ клиенту
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse validationException(final ValidationException e) {
        return new ErrorResponse("Error Validation", e.getMessage());
    }

    /**
     * Метод ошибки ненахождения фильма, юзера или друга
     *
     * @param e - ошибка
     * @return - ответ клиенту
     */
    @ExceptionHandler({FilmException.class, FriendsException.class, UserException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse notFound(final RuntimeException e) {
        return new ErrorResponse("Error NotFound", e.getMessage());
    }

    /**
     * Метод остальных ошибок
     *
     * @param e - ошибка
     * @return - ответ клиенту
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse serverError(final Throwable e) {
        return new ErrorResponse("Error on server", e.getMessage());
    }
}
