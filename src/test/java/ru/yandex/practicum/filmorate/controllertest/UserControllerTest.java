package ru.yandex.practicum.filmorate.controllertest;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exeptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserControllerTest {

    private UserController userController;

    @BeforeEach
    public void setUp() {
        userController = new UserController();
    }

    @Test
    public void shouldFailValidationForEmptyEmail() {
        User user = new User(1, "", "validLogin", "Valid Name", LocalDate.of(1990, 1, 1));
        assertThrows(ValidationException.class, () -> userController.createUser(user));
    }

    @Test
    public void shouldFailValidationForLoginWithSpaces() {
        User user = new User(2, "email@example.com", "invalid login", "Valid Name", LocalDate.of(1990, 1, 1));

        assertThrows(ValidationException.class, () -> userController.createUser(user));
    }

    @Test
    public void shouldFailValidationForFutureBirthday() {
        User user = new User(3, "email@example.com", "validLogin", "Valid Name", LocalDate.of(2099, 1, 1));

        assertThrows(ValidationException.class, () -> userController.createUser(user));
    }
}
