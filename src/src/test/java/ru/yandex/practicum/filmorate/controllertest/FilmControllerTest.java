package ru.yandex.practicum.filmorate.controllertest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {
    private FilmController filmController;

    @BeforeEach
    void setUp() {
        InMemoryFilmStorage filmStorage = new InMemoryFilmStorage();
        FilmService filmService = new FilmService(filmStorage);
        filmController = new FilmController(filmService);
    }

    @Test
    void createFilmShouldAddFilm() {
        Film film = new Film();
        film.setName("Inception");
        film.setDescription("A mind-bending thriller");
        film.setReleaseDate(LocalDate.of(2010, 7, 16));
        film.setDuration(148);

        Film createdFilm = filmController.createFilm(film);

        assertNotNull(createdFilm);
        assertEquals(1, createdFilm.getId());
        assertEquals("Inception", createdFilm.getName());
    }

    @Test
    void updateFilmShouldUpdateExistingFilm() {
        Film film = new Film();
        film.setName("Inception");
        film.setDescription("A mind-bending thriller");
        film.setReleaseDate(LocalDate.of(2010, 7, 16));
        film.setDuration(148);
        Film createdFilm = filmController.createFilm(film);

        Film updatedFilm = new Film();
        updatedFilm.setId(createdFilm.getId());
        updatedFilm.setName("Interstellar");
        updatedFilm.setDescription("A space exploration epic");
        updatedFilm.setReleaseDate(LocalDate.of(2014, 11, 7));
        updatedFilm.setDuration(169);

        Film result = filmController.updateFilm(updatedFilm);

        assertEquals(createdFilm.getId(), result.getId());
        assertEquals("Interstellar", result.getName());
        assertEquals("A space exploration epic", result.getDescription());
    }

    @Test
    void getFilmShouldReturnFilmById() {
        Film film = new Film();
        film.setName("Inception");
        film.setDescription("A mind-bending thriller");
        film.setReleaseDate(LocalDate.of(2010, 7, 16));
        film.setDuration(148);
        Film createdFilm = filmController.createFilm(film);

        Film fetchedFilm = filmController.getFilm(createdFilm.getId());

        assertNotNull(fetchedFilm);
        assertEquals("Inception", fetchedFilm.getName());
    }

    @Test
    void getAllFilmsShouldReturnAllFilms() {
        Film film1 = new Film();
        film1.setName("Inception");
        film1.setDescription("A mind-bending thriller");
        film1.setReleaseDate(LocalDate.of(2010, 7, 16));
        film1.setDuration(148);
        filmController.createFilm(film1);

        Film film2 = new Film();
        film2.setName("Interstellar");
        film2.setDescription("A space exploration epic");
        film2.setReleaseDate(LocalDate.of(2014, 11, 7));
        film2.setDuration(169);
        filmController.createFilm(film2);

        Collection<Film> allFilms = filmController.getAllFilms();

        assertEquals(2, allFilms.size());
    }

    @Test
    void deleteFilmShouldRemoveFilm() {
        Film film = new Film();
        film.setName("Inception");
        film.setDescription("A mind-bending thriller");
        film.setReleaseDate(LocalDate.of(2010, 7, 16));
        film.setDuration(148);
        Film createdFilm = filmController.createFilm(film);

        Film deletedFilm = filmController.deleteFilm(createdFilm.getId());

        assertEquals(createdFilm.getId(), deletedFilm.getId());
        assertThrows(RuntimeException.class, () -> filmController.getFilm(createdFilm.getId()));
    }

    @Test
    void likeAndUnlikeFilmShouldUpdateLikes() {
        Film film = new Film();
        film.setName("Inception");
        film.setDescription("A mind-bending thriller");
        film.setReleaseDate(LocalDate.of(2010, 7, 16));
        film.setDuration(148);
        Film createdFilm = filmController.createFilm(film);

        filmController.likeTheFilm(createdFilm.getId(), 1);

        Film likedFilm = filmController.getFilm(createdFilm.getId());
        assertTrue(likedFilm.getUsersLikes().contains(1));

        filmController.removeLike(createdFilm.getId(), 1);

        Film unlikedFilm = filmController.getFilm(createdFilm.getId());
        assertFalse(unlikedFilm.getUsersLikes().contains(1));
    }

    @Test
    void getTopOfFilmsShouldReturnMostLikedFilms() {
        Film film1 = new Film();
        film1.setName("Inception");
        film1.setDescription("A mind-bending thriller");
        film1.setReleaseDate(LocalDate.of(2010, 7, 16));
        film1.setDuration(148);
        Film createdFilm1 = filmController.createFilm(film1);

        Film film2 = new Film();
        film2.setName("Interstellar");
        film2.setDescription("A space exploration epic");
        film2.setReleaseDate(LocalDate.of(2014, 11, 7));
        film2.setDuration(169);
        Film createdFilm2 = filmController.createFilm(film2);

        filmController.likeTheFilm(createdFilm1.getId(), 1);
        filmController.likeTheFilm(createdFilm2.getId(), 2);
        filmController.likeTheFilm(createdFilm2.getId(), 3);

        List<Film> topFilms = filmController.getTopOfFilms(1);

        assertEquals(1, topFilms.size());
        assertEquals("Interstellar", topFilms.get(0).getName());
    }
}