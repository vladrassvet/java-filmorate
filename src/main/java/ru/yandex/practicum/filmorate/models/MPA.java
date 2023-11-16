package ru.yandex.practicum.filmorate.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class MPA {

    private final int id;

    @Builder.Default
    private String name = "";
}
