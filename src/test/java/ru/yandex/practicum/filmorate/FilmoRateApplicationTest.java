package ru.yandex.practicum.filmorate;


import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genres;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.film.FilmServiceDb;
import ru.yandex.practicum.filmorate.service.user.UserServiceDb;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.List;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmoRateApplicationTest {
    private final UserServiceDb userServiceDb;
    private final FilmServiceDb filmServiceDb;
    private final UserDbStorage userDbStorage;
    private final FilmDbStorage filmDbStorage;

    @BeforeEach
    public void setUp() {
        User user = User.builder()
                .email("@mail.ru")
                .login("ui")
                .name("Max")
                .birthday(LocalDate.of(1976, 9, 20))
                .build();
        userDbStorage.createUser(user);
        User user1 = User.builder()
                .email("@yandex.ru")
                .login("i")
                .name("III")
                .birthday(LocalDate.of(2005, 11, 4))
                .build();
        User user2 = User.builder()
                .email("@mail.ru")
                .login("tooo")
                .name("Max")
                .birthday(LocalDate.of(1111, 12, 20))
                .build();

        userDbStorage.createUser(user1);
        userDbStorage.createUser(user2);
        Film film1 = Film.builder()
                .name("Film1")
                .description("sss")
                .releaseDate(LocalDate.of(2007,11,9))
                .duration(11)
                .mpa(new Mpa(1))
                .genres(List.of(new Genres(1), new Genres(3)))
                .build();
        Film film2 = Film.builder()
                .name("Film2")
                .description("sss")
                .releaseDate(LocalDate.of(2009,1,9))
                .duration(200)
                .mpa(new Mpa(3))
                .build();
        filmDbStorage.createFilm(film1);
        filmDbStorage.createFilm(film2);
    }

    @Test
    public void testGetUserById() {

        User user1 = userServiceDb.getUserById(1);

        assertThat(user1).hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("email", "@mail.ru")
                .hasFieldOrPropertyWithValue("login", "ui")
                .hasFieldOrPropertyWithValue("name", "Max")
                .hasFieldOrPropertyWithValue("birthday", LocalDate.of(1976, 9, 20));
    }

    @Test
    public void testGetFilmAndGenresById() {

        Film film3 = filmServiceDb.getFilmById(2);
        Film film4 = filmServiceDb.getFilmById(1);

        assertThat(film3).hasFieldOrPropertyWithValue("id", 2L)
                .hasFieldOrPropertyWithValue("name", "Film2")
                .hasFieldOrPropertyWithValue("description", "sss")
                .hasFieldOrPropertyWithValue("releaseDate", LocalDate.of(2009,1,9));
        assertThat(film3.getMpa()).hasFieldOrPropertyWithValue("id", 3);

        assertThat(film4.getGenres().get(0)).hasFieldOrPropertyWithValue("id", 1);
        assertThat(film4.getGenres().get(1)).hasFieldOrPropertyWithValue("id", 3);
    }

    @Test
    public void testFriends() {

        userServiceDb.addFriends(1,2);
        userServiceDb.addFriends(1,3);
        userServiceDb.addFriends(2,3);

        assertEquals(2, userServiceDb.userFriends(1).size());
        assertThat(userServiceDb.userCommonFriends(1,2).get(0))
                .hasFieldOrPropertyWithValue("id", 3L)
                .hasFieldOrPropertyWithValue("email", "@mail.ru")
                .hasFieldOrPropertyWithValue("login", "tooo")
                .hasFieldOrPropertyWithValue("name", "Max")
                .hasFieldOrPropertyWithValue("birthday", LocalDate.of(1111, 12, 13));
        userServiceDb.deleteFriend(1, 2);
        assertEquals(1, userServiceDb.userFriends(1).size());
    }

    @Test
    public void testLikeFilm() {
        filmServiceDb.setLikeFilm(1, 1);
        filmServiceDb.setLikeFilm(2, 2);
        filmServiceDb.setLikeFilm(2, 3);
        filmServiceDb.setLikeFilm(2, 1);

        assertThat(filmServiceDb.getPopularFilms(3).get(0))
                .hasFieldOrPropertyWithValue("id", 2L);
        filmServiceDb.deleteLikeFilm(2,1);
        filmServiceDb.deleteLikeFilm(2,2);
        filmServiceDb.deleteLikeFilm(2,3);
        assertThat(filmServiceDb.getPopularFilms(3).get(0))
                .hasFieldOrPropertyWithValue("id", 1L);
    }
}
