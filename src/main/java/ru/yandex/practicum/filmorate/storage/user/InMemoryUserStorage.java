package ru.yandex.practicum.filmorate.storage.user;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.*;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage{

    @Getter
    private final Map<Long, User> user = new HashMap<>();

    public List<User> getUsers() {
        return new ArrayList<>(user.values());
    }

    public User createUser(User user) {
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

    public User updateUser(User user) {
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
        user.setFriendsId(new HashSet<>());
        user.setLikesFilmsId(new HashSet<>());
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
