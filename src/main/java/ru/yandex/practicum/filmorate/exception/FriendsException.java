package ru.yandex.practicum.filmorate.exception;

/**
 * Класс для исключения по друзьям
 *
 * @author Max Vasilyev
 * @version 1.0
 */
public class FriendsException extends RuntimeException {
    public FriendsException(String message) {
        super(message);
    }
}
