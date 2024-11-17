package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody User user) {
        User createdUser = userService.addUser(user);
        log.info("User created: {}", createdUser);
        return ResponseEntity
                .status(201)
                .body(toDto(createdUser));
    }

    @PutMapping
    public ResponseEntity<UserDto> updateUser(@Valid @RequestBody User user) {
        User updatedUser = userService.updateUser(user);
        log.info("User updated: {}", updatedUser);
        return ResponseEntity.ok(toDto(updatedUser));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable int id) {
        User user = userService.getUserById(id);
        log.info("User retrieved: {}", user);
        return ResponseEntity.ok(toDto(user));
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers(
            @RequestParam(required = false) String sortBy) {
        List<User> users = userService.getAllUsers();

        if ("name".equalsIgnoreCase(sortBy)) {
            users.sort((u1, u2) -> u1.getName().compareToIgnoreCase(u2.getName()));
        }

        log.info("Total users retrieved: {}", users.size());
        return ResponseEntity.ok(users.stream()
                .map(this::toDto)
                .collect(Collectors.toList()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable int id) {
        userService.deleteUser(id);
        log.info("User deleted successfully with ID: {}", id);
        return ResponseEntity.noContent().build();
    }


    private UserDto toDto(User user) {
        return new UserDto(
                user.getId(),
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday()
        );
    }
}
