package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Класс user`ов
 *
 * @author Max Vasilyev
 * @version 1.0
 */
@Data
@Builder
public class User {
    private long id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
    private Set<Long> friendsId;
    private Set<Long> likesFilmsId;
    private FriendshipStatus friendshipStatus;
}
