package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeption.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Film createFilm(Film film) {
        return filmStorage.createFilm(film);
    }

    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    public Film getFilm(int id) {
        return filmStorage.getFilm(id)
                .orElseThrow(() -> new ObjectNotFoundException("Фильм с id = " + id + " не найден"));
    }


    public Film deleteFilm(int id) {
        Film film = filmStorage.getFilm(id)
                .orElseThrow(() -> new ObjectNotFoundException("Фильм с id = " + id + " не найден"));
        filmStorage.deleteFilm(id);
        log.info("Фильм с id = {} был удален", id);
        return film;
    }


    public Collection<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Film likeTheFilm(int filmId, int userId) {
        Film film = filmStorage.getFilm(filmId)
                .orElseThrow(() -> new ObjectNotFoundException("Фильм с id = " + filmId + " не найден"));

        userStorage.getUser(userId)
                .orElseThrow(() -> new ObjectNotFoundException("Пользователь с id = " + userId + " не найден"));

        film.addLike(userId);
        log.info("Пользователь с id: {} поставил лайк фильму с id: {}", userId, filmId);
        return film;
    }



    public Film removeLike(int filmId, int userId) {
        Film film = filmStorage.getFilm(filmId)
                .orElseThrow(() -> new ObjectNotFoundException("Фильм с id = " + filmId + " не найден"));

        if (userStorage.getUser(userId) == null) {
            throw new ObjectNotFoundException("Пользователь с id = " + userId + " не найден");
        }

        film.removeLike(userId);
        log.info("Лайк от пользователя с id: {} был удален с фильма с id: {}", userId, filmId);
        return film;
    }


    public List<Film> getTopOfFilms(Integer count) {
        log.info("Топ лучших фильмов по количеству лайков");
        return filmStorage.getAllFilms().stream()
                .sorted((o1, o2) -> Integer.compare(o2.getUsersLikes().size(), o1.getUsersLikes().size()))
                .limit(count)
                .collect(Collectors.toList());
    }
}
