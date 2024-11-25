package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {

    int id;

    @Email(message = "В адресе электронной почты ошибка")
    String email;

    @NotBlank(message = "Логин не может быть пустым и содержать пробелы")
    @NotEmpty(message = "Логин не может быть пустым")
    String login;

    String name;

    @PastOrPresent(message = "Дата рождения не может быть в будущем")
    LocalDate birthday;

    Set<Integer> friends = new HashSet<>();

    Map<Integer, FriendshipStatus> friendshipStatuses = new HashMap<>();

    public void addFriend(int friendId, FriendshipStatus status) {
        friends.add(friendId);
        friendshipStatuses.put(friendId, status);
    }

    public void removeFriend(int friendId) {
        friends.remove(friendId);
        friendshipStatuses.remove(friendId);
    }

    public FriendshipStatus getFriendshipStatus(int friendId){
        return friendshipStatuses.getOrDefault(friendId, FriendshipStatus.UNCONFIRMED);
    }

    public void updateFriendshipStatus(int friendId, FriendshipStatus newStatus) {
        if (friends.contains(friendId)) {
            friendshipStatuses.put(friendId, newStatus);
        } else {
            throw new IllegalArgumentException("Пользователь с ID " + friendId + " не является другом.");
        }
    }
}
