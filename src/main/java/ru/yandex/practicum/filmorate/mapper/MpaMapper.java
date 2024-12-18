package ru.yandex.practicum.filmorate.mapper;

import ru.yandex.practicum.filmorate.dto.MpaDto;
import ru.yandex.practicum.filmorate.model.Mpa;

public class MpaMapper {

    // Преобразование модели Mpa в DTO
    public static MpaDto toDto(Mpa mpa) {
        MpaDto dto = new MpaDto();
        dto.setId(mpa.getId());
        dto.setName(mpa.getName());
        return dto;
    }

    // Преобразование DTO в модель Mpa
    public static Mpa toModel(MpaDto dto) {
        return new Mpa(dto.getId(), dto.getName());
    }
}
