package ru.yandex.practicum.filmorate.controller;


import lombok.extern.slf4j.Slf4j;
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
@RequestMapping("/users")
@Slf4j
public class UserController {

    private final Map<Integer, User> user = new HashMap<>();

    /**
     * Метод получения списка всех user`ов
     *
     * @return - список всех user`ов
     */
    @GetMapping
    public List<User> getUsers() {
        return new ArrayList<>(user.values());
    }

    /**
     * Метод создания user`а
     *
     * @param user - user
     * @return - сохраненный user
     */
    @PostMapping
    public User createUser(@RequestBody User user) {
        userValidation(user);
        if (this.user.containsKey(user.getId())) {
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
    @PutMapping
    public User updateUser(@RequestBody User user) {
        userValidation(user);
        if (!this.user.containsKey(user.getId())) {
            log.warn("Такого user нет");
            throw new UserException("Такого user нет");
        } else {
            User userFromMap = this.user.get(user.getId());
            setUser(user, userFromMap);
            log.info("user обновлен");
            return userFromMap;
        }
    }

    /**
     * Метод для валидации user`а
     *
     * @param user - user
     */
    private static void userValidation(User user) {
        if (user.getEmail() == null || !user.getEmail().contains("@")) {
            log.warn("Ошибка в Email");
            throw new ValidationException("Ошибка в Email");
        } else if (user.getLogin() == null || user.getLogin().isBlank()) {
            log.warn("Ошибка в логине");
            throw new ValidationException("Ошибка в логине");
        } else if (user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Ошибка в дне рождения");
            throw new ValidationException("Ошибка в дне рождения");
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
            user.setId(this.user.size() + 1);
        }
        this.user.put(user.getId(), user);
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
