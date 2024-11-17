package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Film {

    private int id;

    @NotBlank(message = "Film name cannot be empty.")
    private String name;

    @Size(max = 200, message = "Description must be 200 characters or less.")
    private String description;

    @NotNull(message = "Release date cannot be null.")
    @PastOrPresent(message = "Release date cannot be in the future.")
    private LocalDate releaseDate;

    @Positive(message = "Duration must be a positive number.")
    private int duration;
}
