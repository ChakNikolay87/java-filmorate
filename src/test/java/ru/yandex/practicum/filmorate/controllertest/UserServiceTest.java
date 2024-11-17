package ru.yandex.practicum.filmorate.controllertest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exeptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {

    private UserService userService;

    @BeforeEach
    public void setUp() {
        UserStorage userStorage = new InMemoryUserStorage();
        userService = new UserService(userStorage);
    }

    @Test
    void shouldThrowExceptionIfEmailIsInvalid() {
        User user = new User(1, "invalidEmail", "login", "name", LocalDate.of(2000, 1, 1));
        assertThrows(ValidationException.class, () -> userService.addUser(user), "Invalid email format.");
    }

    @Test
    void shouldThrowExceptionIfLoginIsEmpty() {
        User user = new User(1, "user@example.com", "", "name", LocalDate.of(2000, 1, 1));
        assertThrows(ValidationException.class, () -> userService.addUser(user), "Login cannot be empty or contain spaces.");
    }

    @Test
    void shouldThrowExceptionIfLoginContainsSpaces() {
        User user = new User(1, "user@example.com", "invalid login", "name", LocalDate.of(2000, 1, 1));
        assertThrows(ValidationException.class, () -> userService.addUser(user), "Login cannot be empty or contain spaces.");
    }

    @Test
    void shouldThrowExceptionIfBirthdayIsInFuture() {
        User user = new User(1, "user@example.com", "login", "name", LocalDate.now().plusDays(1));
        assertThrows(ValidationException.class, () -> userService.addUser(user), "Birthday cannot be in the future.");
    }

    @Test
    void shouldDeleteUserSuccessfully() {
        User user = new User(1, "user@example.com", "login", "name", LocalDate.of(2000, 1, 1));
        userService.addUser(user);
        assertTrue(userService.getAllUsers().contains(user), "User should exist before deletion.");
        userService.deleteUser(user.getId());
        assertFalse(userService.getAllUsers().contains(user), "User should be deleted successfully.");
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistentUser() {
        int nonExistentUserId = 999;
        assertThrows(IllegalArgumentException.class, () -> userService.deleteUser(nonExistentUserId), "User not found");
    }
}
