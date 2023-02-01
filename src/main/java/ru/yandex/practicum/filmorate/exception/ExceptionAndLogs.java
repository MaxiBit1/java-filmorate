package ru.yandex.practicum.filmorate.exception;

/**
 * Перечисление ошибок
 * @author Max Vasilyev
 * @version 1.0
 */
public enum ExceptionAndLogs {
    NAME_FILM("Ошибка в названии фильма!"),
    DESCRIPTION_FILM("Ошибка в длине фильма"),
    DATE_FILM("Ошибка в дате релиза фильма"),
    LENGTH_FILM("Ошибка в продолжительности фильма"),
    NO_FILM("Фильма нет"),
    NO_USER("Такого user нет"),
    USER_EMAIL("Ошибка в Email"),
    USER_LOGIN("Ошибка в логине"),
    USER_BIRTHDAY("Ошибка в дне рождения"),
    EXIST_FILM("Фильм уже есть"),
    USER_EXIST("Такой user уже есть"),
    ID_ERROR("Ошибка в id");

    private String description;

    ExceptionAndLogs(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
