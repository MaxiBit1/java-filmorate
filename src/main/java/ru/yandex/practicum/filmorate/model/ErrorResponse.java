package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Класс ошибок
 *
 * @author Max Vasilyev
 * @version 1.0
 */
@Getter
@AllArgsConstructor
public class ErrorResponse {
    private String error;
    private String description;
}
