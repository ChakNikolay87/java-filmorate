package ru.yandex.practicum.filmorate.controller;

import ru.yandex.practicum.filmorate.exeptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/films")
public class FilmController {

    private static final Logger log = LoggerFactory.getLogger(FilmController.class);
    private final Map<Integer, Film> films = new HashMap<>();
    private int idCounter = 1;

    @PostMapping
    public ResponseEntity<?> createFilm(@RequestBody Film film) {
        try {
            validateFilm(film);
            film.setId(idCounter++);
            films.put(film.getId(), film);
            log.info("Film created: {}", film);
            return ResponseEntity.status(HttpStatus.CREATED).body(film);
        } catch (ValidationException e) {
            log.error("Film creation failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("error", e.getMessage()));
        }
    }

    @PutMapping
    public ResponseEntity<?> updateFilm(@RequestBody Film film) {
        try {
            validateFilm(film);
            if (!films.containsKey(film.getId())) {
                log.error("Film with id {} not found", film.getId());
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Collections.singletonMap("error", "Film not found"));
            }
            films.put(film.getId(), film);
            log.info("Film updated: {}", film);
            return ResponseEntity.ok(film);
        } catch (ValidationException e) {
            log.error("Film update failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("error", e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<List<Film>> getAllFilms() {
        return ResponseEntity.ok(new ArrayList<>(films.values()));
    }

    public void validateFilm(Film film) throws ValidationException {
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