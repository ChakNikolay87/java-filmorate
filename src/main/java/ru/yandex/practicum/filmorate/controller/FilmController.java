package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/films")
@Slf4j
@RequiredArgsConstructor
public class FilmController {

    private final FilmService filmService;

    @PostMapping
    public ResponseEntity<FilmDto> createFilm(@Valid @RequestBody Film film) {
        log.debug("Received request to create a film: {}", film);
        Film createdFilm = filmService.addFilm(film);
        log.info("Film created: {}", createdFilm);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDto(createdFilm));
    }

    @PutMapping
    public ResponseEntity<FilmDto> updateFilm(@Valid @RequestBody Film film) {
        log.debug("Received request to update a film: {}", film);
        Film updatedFilm = filmService.updateFilm(film);
        log.info("Film updated: {}", updatedFilm);
        return ResponseEntity.ok(toDto(updatedFilm));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FilmDto> getFilmById(@PathVariable int id) {
        log.debug("Received request to retrieve a film with ID: {}", id);
        Film film = filmService.getFilmById(id);
        log.info("Film retrieved: {}", film);
        return ResponseEntity.ok(toDto(film));
    }

    @GetMapping
    public ResponseEntity<List<FilmDto>> getAllFilms(
            @RequestParam(required = false, defaultValue = "id") String sortBy) {
        log.debug("Received request to retrieve all films with sortBy: {}", sortBy);
        List<Film> films = filmService.getAllFilms();

        switch (sortBy.toLowerCase()) {
            case "name" -> films.sort((f1, f2) -> f1.getName().compareToIgnoreCase(f2.getName()));
            case "releaseDate" -> films.sort(Comparator.comparing(Film::getReleaseDate));
            default -> log.warn("Unsupported sortBy parameter: {}, defaulting to unsorted list.", sortBy);
        }

        log.info("Total films retrieved: {}", films.size());
        return ResponseEntity.ok(films.stream().map(this::toDto).collect(Collectors.toList()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFilm(@PathVariable int id) {
        log.debug("Received request to delete a film with ID: {}", id);
        filmService.deleteFilm(id);
        log.info("Film deleted successfully with ID: {}", id);
        return ResponseEntity.noContent().build();
    }

    private FilmDto toDto(Film film) {
        return new FilmDto(
                film.getId(),
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration()
        );
    }
}
