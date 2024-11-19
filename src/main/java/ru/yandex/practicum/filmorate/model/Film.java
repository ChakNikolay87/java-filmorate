package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Film {

    int id;

    @NotBlank(message = "Логин не может быть пустым и содержать пробелы")
    String name;

    @Size(min = 1, max = 200, message = "Максимальная длина описания — 200 символов")
    String description;

    LocalDate releaseDate;

    @Positive(message = "Продолжительность фильма должна быть положительной")
    int duration;

    Set<Integer> usersLikes = new HashSet<>();
}