package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.time.LocalDate;
import java.util.List;

@Service
public class FilmService {
    private static final Logger log = LoggerFactory.getLogger(FilmService.class);
    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Film addFilm(Film film) {
        validateFilm(film);
        log.debug("Attempting to add film: {}", film);
        Film createdFilm = filmStorage.addFilm(film);
        log.info("Film added successfully with ID: {}", createdFilm.getId());
        return createdFilm;
    }

    public Film updateFilm(Film film) {
        validateFilm(film);
        if (filmStorage.getFilmById(film.getId()).isEmpty()) {
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
        if (filmStorage.getFilmById(id).isEmpty()) {
            log.error("Film with ID {} not found, deletion aborted", id);
            throw new IllegalArgumentException("Film not found");
        }
        filmStorage.deleteFilm(id);
        log.info("Film deleted successfully with ID: {}", id);
    }

    private void validateFilm(Film film) {
        if (film.getName() == null || film.getName().isEmpty()) {
            throw new ValidationException("Film name cannot be empty.");
        }
        if (film.getDescription() != null && film.getDescription().length() > 200) {
            throw new ValidationException("Description must be 200 characters or less.");
        }
        LocalDate earliestReleaseDate = LocalDate.of(1895, 12, 28);
        if (film.getReleaseDate() != null && film.getReleaseDate().isBefore(earliestReleaseDate)) {
            throw new ValidationException("Release date cannot be earlier than December 28, 1895.");
        }
        if (film.getDuration() <= 0) {
            throw new ValidationException("Duration must be a positive number.");
        }
    }
}
