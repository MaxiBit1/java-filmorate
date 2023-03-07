package ru.yandex.practicum.filmorate.service.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ExceptionAndLogs;
import ru.yandex.practicum.filmorate.exception.UserException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.util.List;

@Service("UserServiceDb")
@Slf4j
public class UserServiceDb implements UserService{

    private final JdbcTemplate jdbcTemplate;
    @Autowired
    private UserDbStorage userDbStorage;

    @Autowired
    public UserServiceDb(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User getUserById(long idUser) {
        if (!userDbStorage.checkUserId(idUser)) {
            log.warn(ExceptionAndLogs.ID_ERROR.getDescription());
            throw new UserException(ExceptionAndLogs.ID_ERROR.getDescription());
        }
        String sqlRequest = "SELECT * FROM users WHERE id_user = ?";
        return jdbcTemplate.query(sqlRequest, (rs, rowNow) -> userDbStorage.makeUser(rs), idUser)
                .stream()
                .findFirst()
                .get();
    }

    @Override
    public void addFriends(long idUser, long idFriend) {
        if (!userDbStorage.checkUserId(idUser) || !userDbStorage.checkUserId(idFriend)) {
            log.warn(ExceptionAndLogs.ID_ERROR.getDescription());
            throw new UserException(ExceptionAndLogs.ID_ERROR.getDescription());
        }
        String sqlRequest = "INSERT INTO friends (id_user, id_friends, friendship_status) " +
                "VALUES(?,?,?)";
        jdbcTemplate.update(sqlRequest,
                idUser,
                idFriend,
                false);
        jdbcTemplate.update(sqlRequest,
                idFriend,
                idUser,
                true);
    }

    @Override
    public void deleteFriend(long idUser, long idFriend) {
        if (!userDbStorage.checkUserId(idUser) || !userDbStorage.checkUserId(idFriend)) {
            log.warn(ExceptionAndLogs.ID_ERROR.getDescription());
            throw new UserException(ExceptionAndLogs.ID_ERROR.getDescription());
        }
        String sqlRequest = "DELETE FROM friends WHERE id_user = ? AND id_friends = ?";
        jdbcTemplate.update(sqlRequest, idUser, idFriend);
        jdbcTemplate.update(sqlRequest, idFriend, idUser);
    }

    @Override
    public List<User> userFriends(long idUser) {
        if (!userDbStorage.checkUserId(idUser)) {
            log.warn(ExceptionAndLogs.ID_ERROR.getDescription());
            throw new UserException(ExceptionAndLogs.ID_ERROR.getDescription());
        }
        String sqlRequest = "SELECT us.id_user,\n" +
                "       us.email,\n" +
                "       us.login,\n" +
                "       us.name,\n" +
                "       us.birthday\n" +
                "FROM users AS us\n" +
                "INNER JOIN friends AS f on us.id_user = f.id_user\n" +
                "WHERE f.friendship_status = true\n" +
                "        AND id_friends = ?";
        return jdbcTemplate.query(sqlRequest, (rs, rowNum) -> userDbStorage.makeUser(rs), idUser);
    }

    @Override
    public List<User> userCommonFriends(long idUser, long idOtherUser) {
        if (!userDbStorage.checkUserId(idUser) || !userDbStorage.checkUserId(idOtherUser)) {
            log.warn(ExceptionAndLogs.ID_ERROR.getDescription());
            throw new UserException(ExceptionAndLogs.ID_ERROR.getDescription());
        }
        String sqlRequest = "SELECT us.id_user,\n" +
                "       us.email,\n" +
                "       us.login,\n" +
                "       us.name,\n" +
                "       us.birthday\n" +
                "FROM users AS us\n" +
                "INNER JOIN friends AS f on us.id_user = f.id_user\n" +
                "WHERE  us.id_user IN (SELECT fr.id_user\n" +
                "                        FROM friends AS fr\n" +
                "                        WHERE fr.id_friends = ?\n" +
                "                                AND fr.friendship_status = true)\n" +
                "        AND f.id_friends = ? AND friendship_status = true";
        return jdbcTemplate.query(sqlRequest, (rs, rowNum) -> userDbStorage.makeUser(rs), idUser, idOtherUser);
    }
}
