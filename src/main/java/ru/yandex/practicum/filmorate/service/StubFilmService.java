package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

public class StubFilmService extends FilmService {
    private final Map<Long, Film> films = new HashMap<>();
    private final Map<Long, Set<Long>> likes = new HashMap<>();

    public StubFilmService() {
        super(null, null, null, null);
    }

    public void addFilm(Film film) {
        films.put(film.getId(), film);
        likes.put(film.getId(), new HashSet<>());
    }

    @Override
    public Collection<Film> getAll() {
        return films.values();
    }

    @Override
    public Film get(long filmId) {
        if (!films.containsKey(filmId)) {
            throw new NotFoundException("Film not found: " + filmId);
        }
        return films.get(filmId);
    }

    @Override
    public Film create(Film film) {
        films.put(film.getId(), film);
        likes.put(film.getId(), new HashSet<>());
        return film;
    }

    @Override
    public Film update(Film newFilm) {
        if (!films.containsKey(newFilm.getId())) {
            throw new NotFoundException("Film not found: " + newFilm.getId());
        }
        films.put(newFilm.getId(), newFilm);
        return newFilm;
    }

    @Override
    public void addLike(Long filmId, Long userId) {
        if (!films.containsKey(filmId)) {
            throw new NotFoundException("Film not found: " + filmId);
        }
        likes.get(filmId).add(userId);
    }

    @Override
    public void removeLike(Long filmId, Long userId) {
        if (!films.containsKey(filmId)) {
            throw new NotFoundException("Film not found: " + filmId);
        }
        likes.get(filmId).remove(userId);
    }

    public Set<Long> getLikes(long filmId) {
        return likes.get(filmId);
    }
}
