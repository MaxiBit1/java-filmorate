package ru.yandex.practicum.filmorate.service.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserService {

    User getUserById(long idUser);

    /**
     * Добавления друга
     *
     * @param idUser   - айди юзера
     * @param idFriend - айди друга
     */
    void addFriends(long idUser, long idFriend);
    /**
     * Удаления из друзей
     *
     * @param idUser   - айди юзера
     * @param idFriend - айди друга
     */
    void deleteFriend(long idUser, long idFriend);

    /**
     * Получения списка друзей
     *
     * @param idUser - айди юзера
     * @return - список друзей
     */
    List<User> userFriends(long idUser);

    /**
     * Получение общих друзей
     *
     * @param idUser      - айди юзера
     * @param idOtherUser - айди другого юзера
     * @return - список общих друзей
     */
    List<User> userCommonFriends(long idUser, long idOtherUser);


}
