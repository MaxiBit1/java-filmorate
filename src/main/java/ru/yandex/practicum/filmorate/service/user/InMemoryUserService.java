package ru.yandex.practicum.filmorate.service.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ExceptionAndLogs;
import ru.yandex.practicum.filmorate.exception.UserException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Класс бизнес-логики юзеров
 *
 * @author Max Vasilyev
 * @version 1.0
 */
@Slf4j
@Service("InMemoryUserService")
public class InMemoryUserService implements UserService{

    private UserStorage userStorage;

    @Autowired
    public InMemoryUserService(@Qualifier("InMemoryUserStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    /**
     * Получения юзера по айди
     *
     * @param idUser - айди юзера
     * @return - юзер
     */
    @Override
    public User getUserById(long idUser) {
        if (idUser < 0 || idUser > userStorage.getUsers().size()) {
            log.warn(ExceptionAndLogs.ID_ERROR.getDescription());
            throw new UserException(ExceptionAndLogs.ID_ERROR.getDescription());
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
    @Override
    public void addFriends(long idUser, long idFriend) {
        if (idUser < 0
                || idUser > userStorage.getUsers().size()
                || idFriend < 0
                || idFriend > userStorage.getUsers().size()) {
            log.warn(ExceptionAndLogs.ID_ERROR.getDescription());
            throw new UserException(ExceptionAndLogs.ID_ERROR.getDescription());
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
    @Override
    public void deleteFriend(long idUser, long idFriend) {
        if (idUser < 0
                || idUser > userStorage.getUsers().size()
                || idFriend < 0
                || idFriend > userStorage.getUsers().size()) {
            log.warn(ExceptionAndLogs.ID_ERROR.getDescription());
            throw new UserException(ExceptionAndLogs.ID_ERROR.getDescription());
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
    @Override
    public List<User> userFriends(long idUser) {
        if (idUser < 0 || idUser > userStorage.getUsers().size()) {
            log.warn(ExceptionAndLogs.ID_ERROR.getDescription());
            throw new UserException(ExceptionAndLogs.ID_ERROR.getDescription());
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
    @Override
    public List<User> userCommonFriends(long idUser, long idOtherUser) {

        Set<User> userFriends = new HashSet<>(userFriends(idUser));
        Set<User> otherUserFriends = new HashSet<>(userFriends(idOtherUser));

        if (idUser < 0
                || idUser > userStorage.getUsers().size()
                || idOtherUser < 0
                || idOtherUser > userStorage.getUsers().size()) {
            log.warn(ExceptionAndLogs.ID_ERROR.getDescription());
            throw new UserException(ExceptionAndLogs.ID_ERROR.getDescription());
        }
        log.info("Список общих друзей получен");
        return userFriends.stream().filter(otherUserFriends::contains).collect(Collectors.toList());
    }

}
