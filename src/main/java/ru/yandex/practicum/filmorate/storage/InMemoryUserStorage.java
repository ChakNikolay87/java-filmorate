package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private int id = 0;
    protected Map<Integer, User> users = new HashMap<>();

    @Override
    public User createUser(User user) {
        id++;
        user.setId(id);
        users.put(id, user);
        log.info("Был добавлен пользователь {}", user.getName());
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
        } else {
            throw new ObjectNotFoundException("Пользователь не найден.");
        }
        log.info("Информация о пользователе {} была обновлена", user.getName());
        return user;
    }


    @Override
    public Optional<User> getUser(int id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public Optional<User> deleteUser(int id) {
        return Optional.ofNullable(users.remove(id));
    }


    @Override
    public Collection<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }
}