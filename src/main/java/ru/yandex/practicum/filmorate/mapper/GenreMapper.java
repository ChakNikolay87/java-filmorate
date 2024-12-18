package ru.yandex.practicum.filmorate.mapper;

import ru.yandex.practicum.filmorate.dto.GenreDto;
import ru.yandex.practicum.filmorate.model.Genre;

public class GenreMapper {

    // Преобразование модели Genre в DTO
    public static GenreDto toDto(Genre genre) {
        GenreDto dto = new GenreDto();
        dto.setId(genre.getId());
        dto.setName(genre.getName());
        return dto;
    }

    // Преобразование DTO в модель Genre
    public static Genre toModel(GenreDto dto) {
        return new Genre(dto.getId(), dto.getName());
    }
}
