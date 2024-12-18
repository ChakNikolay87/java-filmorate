package ru.yandex.practicum.filmorate.mapper;

import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.model.User;

public class UserMapper {

    public static User toModel(UserDto dto) {
        return User.builder()
                .id(dto.getId())
                .email(dto.getEmail())
                .login(dto.getLogin())
                .name(dto.getName() != null ? dto.getName() : dto.getLogin())
                .birthday(dto.getBirthday())
                .build();
    }

    public static UserDto toDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setLogin(user.getLogin());
        dto.setName(user.getName());
        dto.setBirthday(user.getBirthday());
        return dto;
    }
}
