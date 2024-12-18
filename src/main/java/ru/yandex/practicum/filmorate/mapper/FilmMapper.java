package ru.yandex.practicum.filmorate.mapper;

import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.stream.Collectors;

public class FilmMapper {

    public static Film toModel(FilmDto dto) {
        Film film = Film.builder()
                .id(dto.getId())
                .name(dto.getName())
                .description(dto.getDescription())
                .releaseDate(dto.getReleaseDate())
                .duration(dto.getDuration())
                .mpa(dto.getMpaId() != null ? new Mpa(dto.getMpaId(), null) : null)
                .build();

        if (dto.getGenreIds() != null) {
            film.getGenres().addAll(
                    dto.getGenreIds().stream()
                            .map(id -> new Genre(id, null))
                            .collect(Collectors.toSet())
            );
        }

        return film;
    }

    public static FilmDto toDto(Film film) {
        FilmDto dto = new FilmDto();
        dto.setId(film.getId());
        dto.setName(film.getName());
        dto.setDescription(film.getDescription());
        dto.setReleaseDate(film.getReleaseDate());
        dto.setDuration(film.getDuration());
        dto.setMpaId(film.getMpa() != null ? film.getMpa().getId() : null);
        dto.setGenreIds(film.getGenres().stream()
                .map(Genre::getId)
                .collect(Collectors.toSet()));
        return dto;
    }
}
