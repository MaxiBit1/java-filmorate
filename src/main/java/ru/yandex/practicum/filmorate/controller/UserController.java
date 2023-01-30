package ru.yandex.practicum.filmorate.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;


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

    private UserStorage userStorage;
    private UserService userService;

    @Autowired
    public UserController(UserStorage userStorage, UserService userService) {
        this.userStorage = userStorage;
        this.userService = userService;
    }

    /**
     * Метод получения списка всех user`ов
     *
     * @return - список всех user`ов
     */
    @GetMapping
    public List<User> getUsers() {
        return userStorage.getUsers();
    }

    /**
     * Метод создания user`а
     *
     * @param user - user
     * @return - сохраненный user
     */
    @PostMapping
    public User createUser(@RequestBody User user) {
        return userStorage.createUser(user);
    }

    /**
     * Метод обновления user`а
     *
     * @param user - user
     * @return обновленный user
     */
    @PutMapping
    public User updateUser(@RequestBody User user) {
        return userStorage.updateUser(user);
    }

    /**
     * Метод получения юзера по id
     *
     * @param id - id юзера
     * @return - юзер
     */
    @GetMapping("/{id}")
    public User getUserById(@PathVariable long id) {
        return userService.getUserById(id);
    }

    /**
     * Метод добавления в друзья
     *
     * @param id       - айди юзера
     * @param friendId - айди друга
     */
    @PutMapping("/{id}/friends/{friendId}")
    public void setFriendsUser(@PathVariable long id, @PathVariable long friendId) {
        userService.addFriends(id, friendId);
    }

    /**
     * Метод удаления друга
     *
     * @param id        - айди юзера
     * @param friendsId - айди друга
     */
    @DeleteMapping("/{id}/friends/{friendsID}")
    public void deleteFriends(@PathVariable long id, @PathVariable("friendsID") long friendsId) {
        userService.delFriend(id, friendsId);
    }

    /**
     * Метод получения списка друзей
     *
     * @param id - айди юзера
     * @return - список друзей
     */
    @GetMapping("/{id}/friends")
    public List<User> userFriends(@PathVariable long id) {
        return userService.userFriends(id);
    }

    /**
     * Метод получения общих друзей
     *
     * @param idUser      - айди юзера
     * @param idNotFriend - айди другого юзера
     * @return - список общих друзей
     */
    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> usersCommonFriends(@PathVariable("id") long idUser, @PathVariable("otherId") long idNotFriend) {
        return userService.userCommonFriends(idUser, idNotFriend);
    }

}
