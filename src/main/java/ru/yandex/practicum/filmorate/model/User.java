package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Класс user`ов
 *
 * @author Max Vasilyev
 * @version 1.0
 */
@Getter
@Setter
@Builder
public class User {
    private long id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
    private Set<Long> friendsId;
    private Set<Long> likesFilmsId;

    public User(long id, String email, String login, String name, LocalDate birthday, Set<Long> friendsId, Set<Long> likesFilmsId) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
        this.friendsId = friendsId;
        this.likesFilmsId = likesFilmsId;
    }
}
