package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Optional;

public interface FilmStorage {
    Film createFilm(Film film);

    Film updateFilm(Film film);

    Film deleteFilm(int id);

    Optional<Film> getFilm(int id);

    Collection<Film> getAllFilms();
}