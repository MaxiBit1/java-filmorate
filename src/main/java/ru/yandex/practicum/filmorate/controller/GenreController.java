package ru.yandex.practicum.filmorate.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genres;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.List;

/**
 * класс контроллер для жанров
 */
@RestController
@RequestMapping("/genres")
@Slf4j
public class GenreController {

    private GenreService genreService;

    @Autowired
    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @GetMapping
    public List<Genres> getGenres() {
        log.info("Выданы все genres");
        return genreService.getGenres();
    }

    @GetMapping("/{id}")
    public Genres getGenresById(@PathVariable int id) {
        log.info("Выдан genre по id");
        return genreService.getGenreById(id);
    }
}
