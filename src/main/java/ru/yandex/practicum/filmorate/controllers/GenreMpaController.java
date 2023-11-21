package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.models.Genre;
import ru.yandex.practicum.filmorate.models.MPA;
import ru.yandex.practicum.filmorate.services.GenreMpaService;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
public class GenreMpaController {

    private GenreMpaService genreMpaService;

    @Autowired
    public GenreMpaController(GenreMpaService genreMpaService) {
        this.genreMpaService = genreMpaService;
    }

    @GetMapping("/genres")
    public List<Genre> getListGenre() {
        return new ArrayList<Genre>(genreMpaService.getListGenre());
    }

    @GetMapping("/genres/{id}")
    public Genre getGenreById(@PathVariable int id) {
        return genreMpaService.getGenreById(id);
    }

    @GetMapping("/mpa")
    public List<MPA> getListMpa() {
        return new ArrayList<MPA>(genreMpaService.getListMpa());
    }

    @GetMapping("/mpa/{id}")
    public MPA getMpaById(@PathVariable int id) {
        return genreMpaService.getMpaById(id);
    }
}
