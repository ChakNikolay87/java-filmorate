package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private int id;

    @Email(message = "Invalid email format.")
    @NotBlank(message = "Email cannot be empty.")
    private String email;

    @NotBlank(message = "Login cannot be empty.")
    @Pattern(regexp = "^[^\\s]+$", message = "Login cannot contain spaces.")
    private String login;

    private String name;

    @Past(message = "Birthday cannot be in the future.")
    private LocalDate birthday;
}
