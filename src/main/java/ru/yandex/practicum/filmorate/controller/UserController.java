package ru.yandex.practicum.filmorate.controller;


import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.UserException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Класс контроллера для users
 *
 * @author Max Vasilyev
 * @version 1.0
 */
@RestController
public class UserController {

    @Getter
    private Map<Integer, User> mapUser = new HashMap<>();
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    /**
     * Метод получения списка всех user`ов
     *
     * @return - список всех user`ов
     */
    @GetMapping("/users")
    public List<User> getUsers() {
        return new ArrayList<>(mapUser.values());
    }

    /**
     * Метод создания user`а
     *
     * @param user - user
     * @return - сохраненный user
     */
    @PostMapping("/users")
    public User createUser(@RequestBody User user) {
        if (user.getEmail() == null || !user.getEmail().contains("@")) {
            log.warn("Ошибка в Email");
            throw new ValidationException("Ошибка в Email");
        } else if (user.getLogin() == null || user.getLogin().isBlank()) {
            log.warn("Ошибка в логине");
            throw new ValidationException("Ошибка в логине");
        } else if (user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Ошибка в дне рождения");
            throw new ValidationException("Ошибка в дне рождения");
        } else if (mapUser.containsKey(user.getId())) {
            log.warn("Такой user уже есть");
            throw new UserException("Такой user уже есть");
        } else {
            log.info("user сохранен");
            addUser(user);
            return user;
        }
    }

    /**
     * Метод обновления user`а
     *
     * @param user - user
     * @return обновленный user
     */
    @PutMapping("/users")
    public User updateUser(@RequestBody User user) {
        if (user.getEmail() == null || !user.getEmail().contains("@")) {
            log.warn("Ошибка в Email");
            throw new ValidationException("Ошибка в Email");
        } else if (user.getLogin() == null || user.getLogin().isBlank()) {
            log.warn("Ошибка в логине");
            throw new ValidationException("Ошибка в логине");
        } else if (user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Ошибка в дне рождения");
            throw new ValidationException("Ошибка в дне рождения");
        } else if (!mapUser.containsKey(user.getId())) {
            log.warn("Такого user нет");
            throw new UserException("Такого user нет");
        } else {
            User userFromMap = mapUser.get(user.getId());
            setUser(user, userFromMap);
            log.info("user обновлен");
            return userFromMap;
        }
    }

    /**
     * Метод для добавления user`а
     *
     * @param user - user
     */
    private void addUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if (user.getId() == 0) {
            user.setId(mapUser.size() + 1);
        }
        mapUser.put(user.getId(), user);
    }

    /**
     * Метод установки атрибутов для user`а
     *
     * @param user        - обновленный user
     * @param userFromMap - user из хранилища
     */
    private void setUser(User user, User userFromMap) {
        userFromMap.setName(user.getName());
        userFromMap.setEmail(user.getEmail());
        userFromMap.setLogin(user.getLogin());
        userFromMap.setBirthday(user.getBirthday());
    }
}
