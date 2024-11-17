package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
@Slf4j
@Validated
public class FilmController {

    private final FilmService filmService;

    private FilmDto convertToDto(Film film) {
        return new FilmDto(
                film.getId(),
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration()
        );
    }

    @PostMapping
    public ResponseEntity<FilmDto> createFilm(@RequestBody @Valid Film film) {
        Film createdFilm = filmService.addFilm(film);
        FilmDto filmDto = convertToDto(createdFilm);
        log.info("Film created: {}", filmDto);
        return ResponseEntity.status(201).body(filmDto);
    }

    @PutMapping
    public ResponseEntity<FilmDto> updateFilm(@RequestBody @Valid Film film) {
        Film updatedFilm = filmService.updateFilm(film);
        FilmDto filmDto = convertToDto(updatedFilm);
        log.info("Film updated: {}", filmDto);
        return ResponseEntity.ok(filmDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FilmDto> getFilmById(@PathVariable int id) {
        Film film = filmService.getFilmById(id);
        FilmDto filmDto = convertToDto(film);
        log.info("Film retrieved: {}", filmDto);
        return ResponseEntity.ok(filmDto);
    }

    @GetMapping
    public ResponseEntity<List<FilmDto>> getAllFilms() {
        List<Film> films = filmService.getAllFilms();
        List<FilmDto> filmDtos = films.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        log.info("Total films retrieved: {}", filmDtos.size());
        return ResponseEntity.ok(filmDtos);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFilm(@PathVariable int id) {
        filmService.deleteFilm(id);
        log.info("Film deleted successfully with ID: {}", id);
        return ResponseEntity.noContent().build();
    }
}
