package ru.yandex.practicum.filmorate.models;

import lombok.Data;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film {

    private int id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;
    private MPA mpa;
    private Set<Genre> genres = new HashSet<>();
    private Integer like;

    public Film() {
    }

    public Film(String name, String description, LocalDate releaseDate, int duration, Integer likes, MPA mpa, Set<Genre> genres) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.like = likes;
        this.mpa = mpa;
        this.genres = genres;
    }
}
