package ru.yandex.practicum.filmorate.controller;


import ru.yandex.practicum.filmorate.exeptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final Map<Integer, User> users = new HashMap<>();
    private int idCounter = 1;

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody User user) {
        try {
            validateUser(user);
            user.setId(idCounter++);
            if (user.getName() == null || user.getName().isEmpty()) {
                user.setName(user.getLogin());
            }
            users.put(user.getId(), user);
            log.info("User created: {}", user);
            return ResponseEntity.status(HttpStatus.CREATED).body(user);
        } catch (ValidationException e) {
            log.error("User creation failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("error", e.getMessage()));
        }
    }

    @PutMapping
    public ResponseEntity<?> updateUser(@RequestBody User user) {
        try {
            validateUser(user);
            if (!users.containsKey(user.getId())) {
                log.error("User with id {} not found", user.getId());
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Collections.singletonMap("error", "User not found"));
            }
            if (user.getName() == null || user.getName().isEmpty()) {
                user.setName(user.getLogin());
            }
            users.put(user.getId(), user);
            log.info("User updated: {}", user);
            return ResponseEntity.ok(user);
        } catch (ValidationException e) {
            log.error("User update failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("error", e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(new ArrayList<>(users.values()));
    }

    public void validateUser(User user) throws ValidationException {
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