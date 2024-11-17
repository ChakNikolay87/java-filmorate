package ru.yandex.practicum.filmorate.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilmService {

    private final FilmStorage filmStorage;

    public Film addFilm(@Valid Film film) {
        log.debug("Attempting to add film: {}", film);
        Film createdFilm = filmStorage.addFilm(film);
        log.info("Film added successfully with ID: {}", createdFilm.getId());
        return createdFilm;
    }

    public Film updateFilm(@Valid Film film) {
        if (!filmStorage.existsById(film.getId())) { // Используем existsById
            log.error("Film with ID {} not found", film.getId());
            throw new IllegalArgumentException("Film not found");
        }
        log.debug("Updating film with ID: {}", film.getId());
        Film updatedFilm = filmStorage.updateFilm(film);
        log.info("Film updated successfully with ID: {}", updatedFilm.getId());
        return updatedFilm;
    }

    public Film getFilmById(int id) {
        log.debug("Attempting to retrieve film by ID: {}", id);
        Film film = filmStorage.getFilmById(id)
                .orElseThrow(() -> {
                    log.error("Film with ID {} not found", id);
                    return new IllegalArgumentException("Film not found");
                });
        log.info("Film retrieved: {}", film);
        return film;
    }

    public List<Film> getAllFilms() {
        log.debug("Retrieving all films");
        List<Film> films = filmStorage.getAllFilms();
        log.info("Total films retrieved: {}", films.size());
        return films;
    }

    public void deleteFilm(int id) {
        log.debug("Attempting to delete film with ID: {}", id);
        if (!filmStorage.existsById(id)) {
            log.error("Film with ID {} not found, deletion aborted", id);
            throw new IllegalArgumentException("Film not found");
        }
        filmStorage.deleteFilm(id);
        log.info("Film deleted successfully with ID: {}", id);
    }
}
