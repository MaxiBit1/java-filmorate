package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.FilmException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.films.Film;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Класс для тестов FilmController
 *
 * @author Max Vasilyev
 * @version 1.0
 */
@SpringBootTest
class FilmControllerTests {

    @Autowired
    private FilmController filmController;

    @Test
    void shouldCreateFilm() {
        Film film = Film.builder()
                .name("Титаник")
                .description("ddsgdsgdsg")
                .releaseDate(LocalDate.of(1997, Month.DECEMBER, 2))
                .duration(23)
                .build();
        filmController.createFilm(film);
        assertEquals(film, filmController.getListFilm().get(1));
    }

    @Test
    void shouldReturnExceptionNullName() {
        Film film = Film.builder()
                .name(null)
                .description("ddsgdsgdsg")
                .releaseDate(LocalDate.of(1997, Month.DECEMBER, 2))
                .duration(23)
                .build();
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> filmController.createFilm(film)
        );
        assertNotNull(exception);
        assertEquals("Ошибка в названии фильма!", exception.getMessage());
        ValidationException exception1 = assertThrows(
                ValidationException.class,
                () -> filmController.updateFilm(film)
        );
        assertNotNull(exception1);
        assertEquals("Ошибка в названии фильма!", exception1.getMessage());
    }

    @Test
    void shouldReturnExceptionBlankName() {
        Film film = Film.builder()
                .name(" ")
                .description("ddsgdsgdsg")
                .releaseDate(LocalDate.of(1997, Month.DECEMBER, 2))
                .duration(23)
                .build();
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> filmController.createFilm(film)
        );
        assertNotNull(exception);
        assertEquals("Ошибка в названии фильма!", exception.getMessage());
        ValidationException exception1 = assertThrows(
                ValidationException.class,
                () -> filmController.updateFilm(film)
        );
        assertNotNull(exception1);
        assertEquals("Ошибка в названии фильма!", exception1.getMessage());
    }

    @Test
    void shouldReturnExceptionDescription() {
        Film film = Film.builder()
                .name("Film")
                .description("d".repeat(201))
                .releaseDate(LocalDate.of(1997, Month.DECEMBER, 2))
                .duration(23)
                .build();
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> filmController.createFilm(film)
        );
        assertNotNull(exception);
        assertEquals("Ошибка в длине фильма", exception.getMessage());
        ValidationException exception1 = assertThrows(
                ValidationException.class,
                () -> filmController.updateFilm(film)
        );
        assertNotNull(exception1);
        assertEquals("Ошибка в длине фильма", exception1.getMessage());
    }

    @Test
    void shouldReturnExceptionReleaseDate() {
        Film film = Film.builder()
                .name("Film")
                .description("d")
                .releaseDate(LocalDate.of(1800, Month.DECEMBER, 21))
                .duration(23)
                .build();
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> filmController.createFilm(film)
        );
        assertNotNull(exception);
        assertEquals("Ошибка в дате релиза фильма", exception.getMessage());
        ValidationException exception1 = assertThrows(
                ValidationException.class,
                () -> filmController.updateFilm(film)
        );
        assertNotNull(exception1);
        assertEquals("Ошибка в дате релиза фильма", exception1.getMessage());
    }

    @Test
    void shouldReturnExceptionDuration() {
        Film film = Film.builder()
                .name("Film")
                .description("d")
                .releaseDate(LocalDate.of(1997, Month.DECEMBER, 2))
                .duration(-1)
                .build();
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> filmController.createFilm(film)
        );
        assertNotNull(exception);
        assertEquals("Ошибка в продолжительности фильма", exception.getMessage());
        ValidationException exception1 = assertThrows(
                ValidationException.class,
                () -> filmController.updateFilm(film)
        );
        assertNotNull(exception1);
        assertEquals("Ошибка в продолжительности фильма", exception1.getMessage());
    }

    @Test
    void shouldReturnExceptionNotCorrectIdFilm() {
        Film film = Film.builder()
                .name("Титаник")
                .description("ddsgdsgdsg")
                .releaseDate(LocalDate.of(1997, Month.DECEMBER, 2))
                .duration(23)
                .build();
        filmController.createFilm(film);
        Film film1 = Film.builder()
                .id(9999)
                .name("Film")
                .description("d")
                .releaseDate(LocalDate.of(1997, Month.DECEMBER, 2))
                .duration(23)
                .build();
        FilmException exception = assertThrows(
                FilmException.class,
                () -> filmController.updateFilm(film1)
        );
        assertEquals("Фильма нет", exception.getMessage());
    }

    @Test
    void shouldUpdateFilmCorrectly() {
        Film film = Film.builder()
                .name("Титаник")
                .description("ddsgdsgdsg")
                .releaseDate(LocalDate.of(1997, Month.DECEMBER, 2))
                .duration(23)
                .build();
        filmController.createFilm(film);
        Film film1 = Film.builder()
                .id(1)
                .name("Film")
                .description("d")
                .releaseDate(LocalDate.of(1997, Month.DECEMBER, 2))
                .duration(23)
                .build();
        filmController.updateFilm(film1);
        assertEquals(film, filmController.getListFilm().get(0));
    }
}
