package ru.yandex.practicum.filmorate.storage.user;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ExceptionAndLogs;
import ru.yandex.practicum.filmorate.exception.UserException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.InMemoryUserService;
import ru.yandex.practicum.filmorate.service.user.ValidationUserClass;

import java.util.*;

@Component("InMemoryUserStorage")
@Slf4j
public class InMemoryUserStorage implements UserStorage{

    @Getter
    private final Map<Long, User> user = new HashMap<>();

    public List<User> getUsers() {
        return new ArrayList<>(user.values());
    }

    public User createUser(User user) {
        ValidationUserClass.userValidation(user);
        if (this.user.containsKey(user.getId())) {
            log.warn(ExceptionAndLogs.USER_EXIST.getDescription());
            throw new UserException(ExceptionAndLogs.USER_EXIST.getDescription());
        } else {
            log.info("user сохранен");
            addUser(user);
            return user;
        }
    }

    public User updateUser(User user) {
        ValidationUserClass.userValidation(user);
        if (!this.user.containsKey(user.getId())) {
            log.warn(ExceptionAndLogs.NO_USER.getDescription());
            throw new UserException(ExceptionAndLogs.NO_USER.getDescription());
        } else {
            User userFromMap = this.user.get(user.getId());
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
