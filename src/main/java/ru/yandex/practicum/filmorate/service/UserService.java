package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userStorage;

    public User addUser(User user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        log.debug("Adding new user: {}", user);
        User createdUser = userStorage.addUser(user);
        log.info("User added successfully with ID: {}", createdUser.getId());
        return createdUser;
    }

    public User updateUser(User user) {
        if (!userStorage.existsById(user.getId())) {
            log.error("User with ID {} not found", user.getId());
            throw new IllegalArgumentException("User not found");
        }
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
        if (!userStorage.existsById(id)) {
            log.error("User with ID {} not found, deletion aborted", id);
            throw new IllegalArgumentException("User not found");
        }
        log.debug("Deleting user with ID: {}", id);
        userStorage.deleteUser(id);
        log.info("User deleted successfully with ID: {}", id);
    }
}
