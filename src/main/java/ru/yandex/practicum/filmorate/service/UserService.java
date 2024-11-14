package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.List;

@Service
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserStorage userStorage;
    private int idCounter = 1;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User addUser(User user) {
        validateUser(user);
        user.setId(idCounter++);
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        log.debug("Adding new user: {}", user);
        User createdUser = userStorage.addUser(user);
        log.info("User added successfully with ID: {}", createdUser.getId());
        return createdUser;
    }

    public User updateUser(User user) {
        validateUser(user);
        log.debug("Updating user with ID: {}", user.getId());
        User updatedUser = userStorage.updateUser(user);
        log.info("User updated successfully with ID: {}", updatedUser.getId());
        return updatedUser;
    }

    public User getUserById(int id) {
        log.debug("Retrieving user by ID: {}", id);
        User user = userStorage.getUserById(id)
                .orElseThrow(() -> {
                    log.error("User with ID {} not found", id);
                    return new IllegalArgumentException("User not found");
                });
        log.info("User retrieved: {}", user);
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
        if (userStorage.getUserById(id).isEmpty()) {
            log.error("User with ID {} not found, deletion aborted", id);
            throw new IllegalArgumentException("User not found");
        }
        userStorage.deleteUser(id);
        log.info("User deleted successfully with ID: {}", id);
    }

    private void validateUser(User user) {
        if (user.getEmail() == null || !user.getEmail().contains("@")) {
            throw new ValidationException("Invalid email format.");
        }
        if (user.getLogin() == null || user.getLogin().isEmpty() || user.getLogin().contains(" ")) {
            throw new ValidationException("Login cannot be empty or contain spaces.");
        }
        if (user.getBirthday() != null && user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Birthday cannot be in the future.");
        }
    }
}
