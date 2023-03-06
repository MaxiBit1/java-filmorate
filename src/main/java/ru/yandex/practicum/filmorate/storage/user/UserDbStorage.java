package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ExceptionAndLogs;
import ru.yandex.practicum.filmorate.exception.UserException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.ValidationUserClass;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * Класс работы с бд юзеров
 * @author Max Vasilyev
 * @version 1.0
 */
@Component("UserDbStorage")
@Slf4j
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public List<User> getUsers() {
        String sqlRequest = "SELECT * FROM users";
        return jdbcTemplate.query(sqlRequest, (rs, rowNum) -> makeUser(rs));
    }

    @Override
    public User createUser(User user) {
        ValidationUserClass.userValidation(user);
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        String sqlCreateUser = "INSERT INTO users (email, login, name, birthday) " +
                "VALUES (?,?,?,?)";
        jdbcTemplate.update(sqlCreateUser,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday());
        String sqlWhereIdUser = "SELECT id_user FROM users WHERE email = ?";
        List<Long> idsUsers = jdbcTemplate.query(sqlWhereIdUser, (rs, rowNum) -> rs.getLong("id_user"),
                user.getEmail());
        user.setId(idsUsers.stream().findFirst().get());
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (!checkUserId(user.getId())) {
            log.warn(ExceptionAndLogs.NO_USER.getDescription());
            throw new UserException(ExceptionAndLogs.NO_USER.getDescription());
        }
        ValidationUserClass.userValidation(user);
        String sqlUpdateUser = "UPDATE users SET email = ?, login = ?, name = ?, birthday = ? WHERE id_user = ?";
        jdbcTemplate.update(sqlUpdateUser,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId());
        String sqlWhereIdUser = "SELECT * FROM users WHERE id_user = ?";
        return jdbcTemplate.query(sqlWhereIdUser, (rs, rowNum) -> makeUser(rs),
                user.getId()).get(0);
    }

    @Override
    public Map<Long, User> getUser() {
        return null;
    }

    /**
     * Метод создания юзера из строки sql
     * @param rs - строка
     * @return - юзер
     * @throws SQLException - исключение sql
     */
    public User makeUser(ResultSet rs) throws SQLException {
        return new User(rs.getLong("id_user"),
                rs.getString("email"),
                rs.getString("login"),
                rs.getString("name"),
                rs.getDate("birthday").toLocalDate(),
                new HashSet<>(),
                new HashSet<>());
    }

    /**
     * Проверка принадлежность юера к таюблице юещзров
     * @param idUser - айди юзера
     * @return - результат проверки
     */
    public boolean checkUserId(long idUser) {
        List<Integer> idUsers = jdbcTemplate.query("SELECT id_user FROM users WHERE id_user = ?",
                (rs, rowNum) -> rs.getInt("id_user"), idUser);
        if(idUsers.isEmpty()) {
            return false;
        }
        return true;
    }
}
