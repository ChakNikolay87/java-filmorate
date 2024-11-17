package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FilmDto createFilm(@RequestBody @Valid Film film) {
        Film createdFilm = filmService.addFilm(film);
        log.info("Film created: {}", createdFilm);
        return convertToDto(createdFilm);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public FilmDto updateFilm(@RequestBody @Valid Film film) {
        Film updatedFilm = filmService.updateFilm(film);
        log.info("Film updated: {}", updatedFilm);
        return convertToDto(updatedFilm);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public FilmDto getFilmById(@PathVariable int id) {
        Film film = filmService.getFilmById(id);
        log.info("Film retrieved: {}", film);
        return convertToDto(film);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<FilmDto> getAllFilms() {
        List<Film> films = filmService.getAllFilms();
        log.info("Total films retrieved: {}", films.size());
        return films.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFilm(@PathVariable int id) {
        filmService.deleteFilm(id);
        log.info("Film deleted successfully with ID: {}", id);
    }

    private FilmDto convertToDto(Film film) {
        return new FilmDto(
                film.getId(),
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration()
        );
    }
}
