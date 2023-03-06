package ru.yandex.practicum.filmorate.storage.film;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ExceptionAndLogs;
import ru.yandex.practicum.filmorate.exception.FilmException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.ImMemoryFilmService;
import ru.yandex.practicum.filmorate.service.film.ValidationFilmClass;

import java.util.*;

@Component("InMemoryFilmStorage")
@Slf4j
public class InMemoryFilmStorage implements FilmStorage{
    @Getter
    private final Map<Long, Film> films = new HashMap<>();

    public List<Film> getListFilm() {
        return new ArrayList<>(films.values());
    }

    public Film createFilm(Film film) {
        ValidationFilmClass.filmValidation(film);
        if(films.containsKey(film.getId())) {
            log.warn(ExceptionAndLogs.EXIST_FILM.getDescription());
            throw new FilmException(ExceptionAndLogs.EXIST_FILM.getDescription());
        } else {
            film.setId(films.size() + 1);
            films.put(film.getId(), film);
            log.info("Фильм создан");
            return film;
        }
    }

    public Film updateFilm(Film film) {
        ValidationFilmClass.filmValidation(film);
        if(!films.containsKey(film.getId())) {
            log.warn(ExceptionAndLogs.NO_FILM.getDescription());
            throw new FilmException(ExceptionAndLogs.NO_FILM.getDescription());
        } else {
            Film filmFromList = films.get(film.getId());
            setAtributeFilm(film, filmFromList);
            log.info("Фильм обновлен");
            return filmFromList;
        }
    }

    /**
     * Установка атбрибутов для фильма
     *
     * @param film         - обновленный фильм
     * @param filmFromList - фильм из хранилища
     */
    private void setAtributeFilm(Film film, Film filmFromList) {
        filmFromList.setName(film.getName());
        filmFromList.setDescription(film.getDescription());
        filmFromList.setReleaseDate(film.getReleaseDate());
        filmFromList.setDuration(film.getDuration());
    }
}
