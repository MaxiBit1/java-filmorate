package ru.yandex.practicum.filmorate.storage.film;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage{
    @Getter
    private final Map<Long, Film> films = new HashMap<>();

    public List<Film> getListFilm() {
        return new ArrayList<>(films.values());
    }

    public Film createFilm(Film film) {
        filmValidation(film);
        if(films.containsKey(film.getId())) {
            log.warn("Фильм уже есть");
            throw new FilmException("Фильм уже есть");
        } else {
            film.setId(films.size() + 1);
            film.setLikes(new HashSet<>());
            films.put(film.getId(), film);
            log.info("Фильм создан");
            return film;
        }
    }

    public Film updateFilm(Film film) {
        filmValidation(film);
        if(!films.containsKey(film.getId())) {
            log.warn("Фильма нет");
            throw new FilmException("Фильма нет");
        } else {
            Film filmFromList = films.get(film.getId());
            setAtributeFilm(film, filmFromList);
            log.info("Фильм обновлен");
            return filmFromList;
        }
    }

    /**
     * Метод для обработки валидации фильма
     *
     * @param film - фильм
     */
    private static void filmValidation(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            log.warn("Ошибка в названии фильма!");
            throw new ValidationException("Ошибка в названии фильма!");
        } else if (film.getDescription().length() > 200) {
            log.warn("Ошибка в длине фильма");
            throw new ValidationException("Ошибка в длине фильма");
        } else if (film.getReleaseDate().isBefore(LocalDate.of(1895, Month.DECEMBER, 27))) {
            log.warn("Ошибка в дате релиза фильма");
            throw new ValidationException("Ошибка в дате релиза фильма");
        } else if (film.getDuration() <= 0) {
            log.warn("Ошибка в продолжительности фильма");
            throw new ValidationException("Ошибка в продолжительности фильма");
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
