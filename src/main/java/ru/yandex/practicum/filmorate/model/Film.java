package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.filmorate.exeption.ObjectNotFoundException;

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

    Set<Genre> genres = new HashSet<>();

    MpaRating mpaRating;

    public void addLike(int userId) {
        usersLikes.add(userId);
    }

    public void removeLike(int userId) {
        if (!usersLikes.contains(userId)) {
            throw new ObjectNotFoundException("Пользователь с id = " + userId + " не ставил лайк фильму");
        }
        usersLikes.remove(userId);
    }

}