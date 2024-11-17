package ru.yandex.practicum.filmorate.controllertest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exeptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FilmServiceTest {

    private FilmService filmService;

    @BeforeEach
    public void setUp() {
        FilmStorage filmStorage = new InMemoryFilmStorage();
        filmService = new FilmService(filmStorage);
    }

    @Test
    void shouldThrowExceptionIfNameIsEmpty() {
        Film film = new Film(1, "", "Some description", LocalDate.of(2020, 1, 1), 120);
        assertThrows(ValidationException.class, () -> filmService.addFilm(film), "Film name cannot be empty.");
    }

    @Test
    void shouldThrowExceptionIfDescriptionExceedsLimit() {
        String longDescription = "A".repeat(201);
        Film film = new Film(1, "Film Name", longDescription, LocalDate.of(2020, 1, 1), 120);
        assertThrows(ValidationException.class, () -> filmService.addFilm(film), "Description must be 200 characters or less.");
    }

    @Test
    void shouldThrowExceptionIfReleaseDateIsTooEarly() {
        Film film = new Film(1, "Film Name", "Description", LocalDate.of(1890, 1, 1), 120);
        assertThrows(ValidationException.class, () -> filmService.addFilm(film), "Release date cannot be earlier than December 28, 1895.");
    }

    @Test
    void shouldThrowExceptionIfDurationIsNegative() {
        Film film = new Film(1, "Film Name", "Description", LocalDate.of(2020, 1, 1), -10);
        assertThrows(ValidationException.class, () -> filmService.addFilm(film), "Duration must be a positive number.");
    }

    @Test
    void shouldDeleteFilmSuccessfully() {
        Film film = new Film(1, "Film Name", "Description", LocalDate.of(2020, 1, 1), 120);
        filmService.addFilm(film);
        assertTrue(filmService.getAllFilms().contains(film), "Film should exist before deletion.");
        filmService.deleteFilm(film.getId());
        assertTrue(filmService.getAllFilms().isEmpty(), "Film should be deleted successfully.");
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistentFilm() {
        int nonExistentFilmId = 999;
        assertThrows(IllegalArgumentException.class, () -> filmService.deleteFilm(nonExistentFilmId), "Film not found");
    }
}