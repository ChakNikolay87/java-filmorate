package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilmService {
    private static final LocalDate EARLIEST_RELEASE_DATE = LocalDate.of(1895, 12, 28);

    private final FilmStorage filmStorage;

    public Film addFilm(Film film) {
        validateFilm(film);
        log.debug("Attempting to add film: {}", film);
        Film createdFilm = filmStorage.addFilm(film);
        log.info("Film added successfully: {}", createdFilm);
        return createdFilm;
    }

    public Film updateFilm(Film film) {
        validateFilm(film);
        if (filmStorage.exists(film.getId())) {
            log.error("Film with ID {} not found, update failed", film.getId());
            throw new IllegalArgumentException("Film with ID " + film.getId() + " not found.");
        }
        log.debug("Updating film with ID: {}", film.getId());
        Film updatedFilm = filmStorage.updateFilm(film);
        log.info("Film updated successfully: {}", updatedFilm);
        return updatedFilm;
    }

    public Film getFilmById(int id) {
        log.debug("Attempting to retrieve film by ID: {}", id);
        Film film = filmStorage.getFilmById(id)
                .orElseThrow(() -> {
                    log.error("Film with ID {} not found", id);
                    return new IllegalArgumentException("Film with ID " + id + " not found.");
                });
        log.info("Film retrieved successfully: {}", film);
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
        if (filmStorage.exists(id)) {
            log.error("Film with ID {} not found, deletion aborted", id);
            throw new IllegalArgumentException("Film with ID " + id + " not found.");
        }
        filmStorage.deleteFilm(id);
        log.info("Film deleted successfully with ID: {}", id);
    }

    private void validateFilm(Film film) {
        if (film.getName() == null || film.getName().isEmpty()) {
            log.error("Validation failed: Film name is empty");
            throw new ValidationException("Film name cannot be empty.");
        }
        if (film.getDescription() != null && film.getDescription().length() > 200) {
            log.error("Validation failed: Film description exceeds 200 characters");
            throw new ValidationException("Description must be 200 characters or less.");
        }
        if (film.getReleaseDate() != null && film.getReleaseDate().isBefore(EARLIEST_RELEASE_DATE)) {
            log.error("Validation failed: Release date is earlier than {}", EARLIEST_RELEASE_DATE);
            throw new ValidationException("Release date cannot be earlier than " + EARLIEST_RELEASE_DATE + ".");
        }
        if (film.getDuration() <= 0) {
            log.error("Validation failed: Film duration is non-positive");
            throw new ValidationException("Duration must be a positive number.");
        }
    }
}
