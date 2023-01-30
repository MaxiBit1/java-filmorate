package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FriendsException;
import ru.yandex.practicum.filmorate.exception.UserException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Класс бизнес-логики юзеров
 *
 * @author Max Vasilyev
 * @version 1.0
 */
@Slf4j
@Service
public class UserService {

    private UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    /**
     * Получения юзера по айди
     *
     * @param idUser - айди юзера
     * @return - юзер
     */
    public User getUserById(long idUser) {
        if (idUser < 0 || idUser > userStorage.getUsers().size()) {
            log.warn("Ошибка в id");
            throw new UserException("Id меньше нуля или равен, больше длине списка юзеров");
        }
        log.info("Получение юзера по id");
        return userStorage.getUsers().get((int) (idUser - 1));
    }

    /**
     * Добавления друга
     *
     * @param idUser   - айди юзера
     * @param idFriend - айди друга
     */
    public void addFriends(long idUser, long idFriend) {
        if (idUser < 0 || idUser > userStorage.getUsers().size()) {
            log.warn("Ошибка в id");
            throw new UserException("Id меньше нуля или равен, больше длине списка юзеров");
        }
        if (idFriend < 0 || idFriend > userStorage.getUsers().size()) {
            log.warn("Ошибка в id друга");
            throw new FriendsException("Id друга меньше нуля или равен, больше длине списка юзеров");
        }
        log.info("Друг добавлен");
        userStorage.getUser().get(idUser).getFriendsId().add(idFriend);
        userStorage.getUser().get(idFriend).getFriendsId().add((long) idUser);
    }

    /**
     * Удаления из друзей
     *
     * @param idUser   - айди юзера
     * @param idFriend - айди друга
     */
    public void delFriend(long idUser, long idFriend) {
        if (idUser < 0 || idUser > userStorage.getUsers().size()) {
            log.warn("Ошибка в id");
            throw new UserException("Id меньше нуля или равен, больше длине списка юзеров");
        }
        if (idFriend < 0 || idFriend > userStorage.getUsers().size()) {
            log.warn("Ошибка в id друга");
            throw new FriendsException("Id друга меньше нуля или равен, больше длине списка юзеров");
        }
        log.info("Друг удален");
        userStorage.getUser().get(idUser).getFriendsId().remove(idFriend);
    }

    /**
     * Получения списка друзей
     *
     * @param idUser - айди юзера
     * @return - список друзей
     */
    public List<User> userFriends(long idUser) {
        if (idUser < 0 || idUser > userStorage.getUsers().size()) {
            log.warn("Ошибка в id");
            throw new UserException("Id меньше нуля или равен, больше длине списка юзеров");
        }
        List<User> resultList = new ArrayList<>();
        for (Long friendsId : userStorage.getUser().get(idUser).getFriendsId()) {
            resultList.add(userStorage.getUsers().stream().filter(user -> user.getId() == friendsId).findFirst().get());
        }
        log.info("Список друзей получен");
        return resultList;
    }

    /**
     * Получение общих друзей
     *
     * @param idUser      - айди юзера
     * @param idOtherUser - айди другого юзера
     * @return - список общих друзей
     */
    public List<User> userCommonFriends(long idUser, long idOtherUser) {
        List<Long> commonFriendsIdList = new ArrayList<>();
        List<User> resultList = new ArrayList<>();

        if (idUser < 0 || idUser > userStorage.getUsers().size()) {
            log.warn("Ошибка в id");
            throw new UserException("Id меньше нуля или равен, больше длине списка юзеров");
        }
        if (idOtherUser < 0 || idOtherUser > userStorage.getUsers().size()) {
            log.warn("Ошибка в id друга");
            throw new FriendsException("Id друга меньше нуля или равен, больше длине списка юзеров");
        }
        for (Long longCheck : userStorage.getUser().get(idUser).getFriendsId()) {
            if (userStorage.getUser().get(idOtherUser).getFriendsId().stream()
                    .anyMatch(x -> Objects.equals(x, longCheck))) {
                commonFriendsIdList.add(longCheck);
            }
        }
        if (!commonFriendsIdList.isEmpty()) {
            for (Long commonFriendId : commonFriendsIdList) {
                resultList.add(userStorage.getUser().get(commonFriendId));
            }
            log.info("Список общих друзей получен");
        }
        return resultList;
    }
}
