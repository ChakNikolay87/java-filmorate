package ru.yandex.practicum.filmorate.controllertest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exeptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class FilmControllerTest {

    private FilmController filmController;

    @BeforeEach
    public void setUp() {
        filmController = new FilmController();
    }

    @Test
    void shouldThrowExceptionIfNameIsEmpty() {
        Film film = new Film(1, "", "Some description", LocalDate.of(2020, 1, 1), 120);
        assertThrows(ValidationException.class, () -> filmController.validateFilm(film), "Film name cannot be empty.");
    }

    @Test
    void shouldThrowExceptionIfDescriptionExceedsLimit() {
        String longDescription = "A".repeat(201);
        Film film = new Film(1, "Film Name", longDescription, LocalDate.of(2020, 1, 1), 120);
        assertThrows(ValidationException.class, () -> filmController.validateFilm(film), "Description must be 200 characters or less.");
    }

    @Test
    void shouldThrowExceptionIfReleaseDateIsTooEarly() {
        Film film = new Film(1, "Film Name", "Description", LocalDate.of(1890, 1, 1), 120);
        assertThrows(ValidationException.class, () -> filmController.validateFilm(film), "Release date cannot be earlier than December 28, 1895.");
    }

    @Test
    void shouldThrowExceptionIfDurationIsNegative() {
        Film film = new Film(1, "Film Name", "Description", LocalDate.of(2020, 1, 1), -10);
        assertThrows(ValidationException.class, () -> filmController.validateFilm(film), "Duration must be a positive number.");
    }
}
