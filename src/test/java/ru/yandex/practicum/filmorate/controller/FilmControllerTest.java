package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.StubFilmService;

import java.time.LocalDate;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {
    private FilmController filmController;
    private StubFilmService filmService;

    @BeforeEach
    void setUp() {
        filmService = new StubFilmService();
        filmController = new FilmController(filmService);
    }

    @Test
    void getAll() {
        Film film1 = Film.builder()
                .id(1L)
                .name("Film 1")
                .description("Description 1")
                .releaseDate(LocalDate.of(2020, 1, 1))
                .duration(120)
                .build();
        Film film2 = Film.builder()
                .id(2L)
                .name("Film 2")
                .description("Description 2")
                .releaseDate(LocalDate.of(2021, 5, 10))
                .duration(90)
                .build();

        filmService.addFilm(film1);
        filmService.addFilm(film2);

        Collection<Film> films = filmController.getAll();

        assertEquals(2, films.size());
    }

    @Test
    void get() {
        Film film = Film.builder()
                .id(1L)
                .name("Film 1")
                .description("Description 1")
                .releaseDate(LocalDate.of(2020, 1, 1))
                .duration(120)
                .build();
        filmService.addFilm(film);

        Film result = filmController.get(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Film 1", result.getName());
    }

    @Test
    void create() {
        Film film = Film.builder()
                .id(1L)
                .name("New Film")
                .description("Description")
                .releaseDate(LocalDate.of(2020, 1, 1))
                .duration(120)
                .build();

        Film result = filmController.create(film);

        assertNotNull(result);
        assertEquals("New Film", result.getName());
        assertEquals(1, filmService.getAll().size());
    }

    @Test
    void update() {
        Film film = Film.builder()
                .id(1L)
                .name("Old Film")
                .description("Description")
                .releaseDate(LocalDate.of(2020, 1, 1))
                .duration(120)
                .build();

        filmService.addFilm(film);

        Film updatedFilm = film.toBuilder().name("Updated Film").build();
        Film result = filmController.update(updatedFilm);

        assertNotNull(result);
        assertEquals("Updated Film", result.getName());
    }

    @Test
    void addLike() {
        Film film = Film.builder()
                .id(1L)
                .name("Film")
                .description("Description")
                .releaseDate(LocalDate.of(2020, 1, 1))
                .duration(120)
                .build();

        filmService.addFilm(film);

        filmController.addLike(1L, 42L);

        assertEquals(1, filmService.getLikes(1L).size());
        assertTrue(filmService.getLikes(1L).contains(42L));
    }

    @Test
    void deleteLike() {
        Film film = Film.builder()
                .id(1L)
                .name("Film")
                .description("Description")
                .releaseDate(LocalDate.of(2020, 1, 1))
                .duration(120)
                .build();

        filmService.addFilm(film);
        filmController.addLike(1L, 42L);

        filmController.deleteLike(1L, 42L);

        assertEquals(0, filmService.getLikes(1L).size());
    }
}
