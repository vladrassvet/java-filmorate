package ru.yandex.practicum.filmorate.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private int id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
    private Set<Integer> friendsId = new HashSet<>();
}
