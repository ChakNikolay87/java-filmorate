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
    void shouldThrowExceptionIfEmailIsInvalid() {
        User user = new User(1, "invalidEmail", "login", "name", LocalDate.of(2000, 1, 1));
        assertThrows(ValidationException.class, () -> userController.validateUser(user), "Invalid email format.");
    }

    @Test
    void shouldThrowExceptionIfLoginIsEmpty() {
        User user = new User(1, "user@example.com", "", "name", LocalDate.of(2000, 1, 1));
        assertThrows(ValidationException.class, () -> userController.validateUser(user), "Login cannot be empty or contain spaces.");
    }

    @Test
    void shouldThrowExceptionIfLoginContainsSpaces() {
        User user = new User(1, "user@example.com", "invalid login", "name", LocalDate.of(2000, 1, 1));
        assertThrows(ValidationException.class, () -> userController.validateUser(user), "Login cannot be empty or contain spaces.");
    }

    @Test
    void shouldThrowExceptionIfBirthdayIsInFuture() {
        User user = new User(1, "user@example.com", "login", "name", LocalDate.now().plusDays(1));
        assertThrows(ValidationException.class, () -> userController.validateUser(user), "Birthday cannot be in the future.");
    }
}

