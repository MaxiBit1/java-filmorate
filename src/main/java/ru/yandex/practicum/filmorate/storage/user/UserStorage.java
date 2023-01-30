package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Map;

/**
 * Интерфейс хранения юзеров
 *
 * @author Max Vasilyev
 * @version 1.0
 */
public interface UserStorage {

    /**
     * Метод получения списка всех user`ов
     *
     * @return - список всех user`ов
     */
    List<User> getUsers();

    /**
     * Метод создания user`а
     *
     * @param user - user
     * @return - сохраненный user
     */
    User createUser(User user);

    /**
     * Метод обновления user`а
     *
     * @param user - user
     * @return обновленный user
     */
    User updateUser(User user);

    Map<Long, User> getUser();
}
