package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeption.InternalServerException;
import ru.yandex.practicum.filmorate.exeption.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User createUser(User user) {
        return userStorage.createUser(user);
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public User getUser(int id) {
        return userStorage.getUser(id)
                .orElseThrow(() -> new ObjectNotFoundException("Пользователь с id = " + id + " не найден"));
    }

    public User deleteUser(int id) {
        return userStorage.deleteUser(id)
                .orElseThrow(() -> new ObjectNotFoundException("Пользователь с id = " + id + " не найден"));
    }

    public Collection<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public List<User> addFriends(int idOfPerson1, int idOfPerson2) {
        User user1 = userStorage.getUser(idOfPerson1)
                .orElseThrow(() -> new ObjectNotFoundException("Пользователь с id = " + idOfPerson1 + " не найден"));
        User user2 = userStorage.getUser(idOfPerson2)
                .orElseThrow(() -> new ObjectNotFoundException("Пользователь с id = " + idOfPerson2 + " не найден"));

        if (user1.getFriends().contains(idOfPerson2)) {
            throw new InternalServerException("Пользователи уже являются друзьями");
        }

        user1.addFriend(idOfPerson2);
        user2.addFriend(idOfPerson1);

        log.info("Пользователи с id: {} и {} теперь друзья", idOfPerson1, idOfPerson2);
        return List.of(user1, user2);
    }


    public List<User> deleteFriends(int idOfPerson1, int idOfPerson2) {
        User user1 = userStorage.getUser(idOfPerson1)
                .orElseThrow(() -> new ObjectNotFoundException("Пользователь с id = " + idOfPerson1 + " не найден"));
        User user2 = userStorage.getUser(idOfPerson2)
                .orElseThrow(() -> new ObjectNotFoundException("Пользователь с id = " + idOfPerson2 + " не найден"));

        user1.removeFriend(idOfPerson2);
        user2.removeFriend(idOfPerson1);

        log.info("Пользователи {} и {} больше не друзья", idOfPerson1, idOfPerson2);
        return List.of(user1, user2);
    }


    public List<User> getFriendsListOfPerson(int id) {
        User user = userStorage.getUser(id)
                .orElseThrow(() -> new ObjectNotFoundException("Пользователь с id = " + id + " не найден"));

        log.info("Список друзей пользователя с id: {}", id);
        return user.getFriends().stream()
                .map(friendId -> userStorage.getUser(friendId)
                        .orElseThrow(() -> new ObjectNotFoundException("Друг с id = " + friendId + " не найден")))
                .collect(Collectors.toList());
    }

    public List<User> getListOfCommonFriends(int idOfPerson1, int idOfPerson2) {
        User firstPerson = userStorage.getUser(idOfPerson1)
                .orElseThrow(() -> new ObjectNotFoundException("Пользователь с id = " + idOfPerson1 + " не найден"));
        User secondPerson = userStorage.getUser(idOfPerson2)
                .orElseThrow(() -> new ObjectNotFoundException("Пользователь с id = " + idOfPerson2 + " не найден"));

        log.info("Список общих друзей пользователей с id: {} и {}", idOfPerson1, idOfPerson2);

        return firstPerson.getFriends().stream()
                .filter(secondPerson.getFriends()::contains)
                .map(friendId -> userStorage.getUser(friendId)
                        .orElseThrow(() -> new ObjectNotFoundException("Друг с id = " + friendId + " не найден")))
                .collect(Collectors.toList());
    }
}
