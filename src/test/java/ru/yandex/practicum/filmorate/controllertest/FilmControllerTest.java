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
    public void shouldFailValidationForEmptyName() {
        Film film = new Film(1, "", "Valid description", LocalDate.of(2000, 1, 1), 120);
        assertThrows(ValidationException.class, () -> filmController.addFilm(film));
    }

    @Test
    public void shouldFailValidationForLongDescription() {
        String longDescription = "a".repeat(201);
        Film film = new Film(2, "Valid Name", longDescription, LocalDate.of(2000, 1, 1), 120);
        assertThrows(ValidationException.class, () -> filmController.addFilm(film));
    }

    @Test
    public void shouldFailValidationForNegativeDuration() {
        Film film = new Film(3, "Valid Name", "Valid description", LocalDate.of(2000, 1, 1), -120);
        assertThrows(ValidationException.class, () -> filmController.addFilm(film));
    }
}
