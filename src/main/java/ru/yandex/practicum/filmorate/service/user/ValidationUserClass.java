package ru.yandex.practicum.filmorate.service.user;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.ExceptionAndLogs;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

/**
 * Класс валидации юзеров
 * @author Max Vasilyev
 * @version 1.0
 */
@Slf4j
public class ValidationUserClass {
    /**
     * Метод для валидации user`а
     *
     * @param user - user
     */
    public static void userValidation(User user) {
        if (user.getEmail() == null || !user.getEmail().contains("@")) {
            log.warn(ExceptionAndLogs.USER_EMAIL.getDescription());
            throw new ValidationException(ExceptionAndLogs.USER_EMAIL.getDescription());
        } else if (user.getLogin() == null || user.getLogin().isBlank()) {
            log.warn(ExceptionAndLogs.USER_LOGIN.getDescription());
            throw new ValidationException(ExceptionAndLogs.USER_LOGIN.getDescription());
        } else if (user.getBirthday().isAfter(LocalDate.now())) {
            log.warn(ExceptionAndLogs.USER_BIRTHDAY.getDescription());
            throw new ValidationException(ExceptionAndLogs.USER_BIRTHDAY.getDescription());
        }
    }
}
