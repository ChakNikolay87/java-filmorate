package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    public User addUser(User user) {
        validateUser(user);
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        log.debug("Adding new user: {}", user);
        User createdUser = userStorage.addUser(user);
        log.info("User added successfully: {}", createdUser);
        return createdUser;
    }

    public User updateUser(User user) {
        validateUser(user);
        if (userStorage.exists(user.getId())) {
            log.error("User with ID {} not found, update failed", user.getId());
            throw new IllegalArgumentException("User with ID " + user.getId() + " not found.");
        }
        log.debug("Updating user with ID: {}", user.getId());
        User updatedUser = userStorage.updateUser(user);
        log.info("User updated successfully: {}", updatedUser);
        return updatedUser;
    }

    public User getUserById(int id) {
        log.debug("Retrieving user by ID: {}", id);
        User user = userStorage.getUserById(id)
                .orElseThrow(() -> {
                    log.error("User with ID {} not found", id);
                    return new IllegalArgumentException("User with ID " + id + " not found.");
                });
        log.info("User retrieved successfully: {}", user);
        return user;
    }

    public List<User> getAllUsers() {
        log.debug("Retrieving all users");
        List<User> users = userStorage.getAllUsers();
        log.info("Total users found: {}", users.size());
        return users;
    }

    public void deleteUser(int id) {
        log.debug("Attempting to delete user with ID: {}", id);
        if (userStorage.exists(id)) {
            log.error("User with ID {} not found, deletion aborted", id);
            throw new IllegalArgumentException("User with ID " + id + " not found.");
        }
        userStorage.deleteUser(id);
        log.info("User deleted successfully with ID: {}", id);
    }

    private void validateUser(User user) {
        if (user.getEmail() == null || !user.getEmail().contains("@")) {
            log.error("Validation failed: Invalid email format for user: {}", user);
            throw new ValidationException("Invalid email format.");
        }
        if (user.getLogin() == null || user.getLogin().isEmpty() || user.getLogin().contains(" ")) {
            log.error("Validation failed: Invalid login for user: {}", user);
            throw new ValidationException("Login cannot be empty or contain spaces.");
        }
        if (user.getBirthday() != null && user.getBirthday().isAfter(LocalDate.now())) {
            log.error("Validation failed: Birthday is in the future for user: {}", user);
            throw new ValidationException("Birthday cannot be in the future.");
        }
    }
}
