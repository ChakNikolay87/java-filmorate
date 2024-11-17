package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Film {

    private int id;

    @NotNull(message = "Film name cannot be null")
    @Size(min = 1, max = 100, message = "Film name must be between 1 and 100 characters")
    private String name;

    @Size(max = 200, message = "Description must be 200 characters or less")
    private String description;

    @Past(message = "Release date must be a past date")
    private LocalDate releaseDate;

    @Min(value = 1, message = "Duration must be a positive number")
    private int duration;
}
