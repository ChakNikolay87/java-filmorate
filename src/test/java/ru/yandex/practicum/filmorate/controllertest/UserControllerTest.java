package ru.yandex.practicum.filmorate.controllertest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    private UserController userController;

    @BeforeEach
    void setUp() {
        InMemoryUserStorage userStorage = new InMemoryUserStorage();
        UserService userService = new UserService(userStorage);
        userController = new UserController(userService);
    }

    @Test
    void createUserShouldAddUser() {
        User user = new User();
        user.setEmail("user1@example.com");
        user.setLogin("user1");
        user.setName("User One");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        User createdUser = userController.createUser(user);

        assertNotNull(createdUser);
        assertEquals(1, createdUser.getId());
        assertEquals("user1", createdUser.getLogin());
    }

    @Test
    void updateUserShouldUpdateExistingUser() {
        User user = new User();
        user.setEmail("user1@example.com");
        user.setLogin("user1");
        user.setName("User One");
        user.setBirthday(LocalDate.of(1990, 1, 1));
        User createdUser = userController.createUser(user);

        User updatedUser = new User();
        updatedUser.setId(createdUser.getId());
        updatedUser.setEmail("updated_user@example.com");
        updatedUser.setLogin("updated_user");
        updatedUser.setName("Updated User");
        updatedUser.setBirthday(LocalDate.of(1985, 5, 15));

        User result = userController.updateUser(updatedUser);

        assertEquals("updated_user", result.getLogin());
        assertEquals("Updated User", result.getName());
        assertEquals("updated_user@example.com", result.getEmail());
    }

    @Test
    void getUserShouldReturnUserById() {
        User user = new User();
        user.setEmail("user1@example.com");
        user.setLogin("user1");
        user.setName("User One");
        user.setBirthday(LocalDate.of(1990, 1, 1));
        User createdUser = userController.createUser(user);

        User fetchedUser = userController.getUser(createdUser.getId());

        assertNotNull(fetchedUser);
        assertEquals("user1", fetchedUser.getLogin());
        assertEquals("User One", fetchedUser.getName());
    }

    @Test
    void deleteUserShouldRemoveUser() {
        User user = new User();
        user.setEmail("user1@example.com");
        user.setLogin("user1");
        user.setName("User One");
        user.setBirthday(LocalDate.of(1990, 1, 1));
        User createdUser = userController.createUser(user);

        User deletedUser = userController.deleteUser(createdUser.getId());

        assertNotNull(deletedUser);
        assertEquals(createdUser.getId(), deletedUser.getId());
        assertThrows(RuntimeException.class, () -> userController.getUser(createdUser.getId()));
    }

    @Test
    void addFriendsShouldEstablishFriendship() {
        User user1 = new User();
        user1.setEmail("user1@example.com");
        user1.setLogin("user1");
        user1.setName("User One");
        user1.setBirthday(LocalDate.of(1990, 1, 1));
        User createdUser1 = userController.createUser(user1);

        User user2 = new User();
        user2.setEmail("user2@example.com");
        user2.setLogin("user2");
        user2.setName("User Two");
        user2.setBirthday(LocalDate.of(1992, 2, 2));
        User createdUser2 = userController.createUser(user2);

        List<User> friends = userController.addFriends(createdUser1.getId(), createdUser2.getId());

        assertEquals(2, friends.size());
        assertTrue(createdUser1.getFriends().contains(createdUser2.getId()));
        assertTrue(createdUser2.getFriends().contains(createdUser1.getId()));
    }

    @Test
    void deleteFriendsShouldRemoveFriendship() {
        User user1 = new User();
        user1.setEmail("user1@example.com");
        user1.setLogin("user1");
        user1.setName("User One");
        user1.setBirthday(LocalDate.of(1990, 1, 1));
        User createdUser1 = userController.createUser(user1);

        User user2 = new User();
        user2.setEmail("user2@example.com");
        user2.setLogin("user2");
        user2.setName("User Two");
        user2.setBirthday(LocalDate.of(1992, 2, 2));
        User createdUser2 = userController.createUser(user2);

        userController.addFriends(createdUser1.getId(), createdUser2.getId());

        List<User> friendsAfterRemoval = userController.deleteFriends(createdUser1.getId(), createdUser2.getId());

        assertFalse(createdUser1.getFriends().contains(createdUser2.getId()));
        assertFalse(createdUser2.getFriends().contains(createdUser1.getId()));
        assertEquals(2, friendsAfterRemoval.size());
    }

    @Test
    void getFriendsListOfPersonShouldReturnFriendList() {
        User user1 = new User();
        user1.setEmail("user1@example.com");
        user1.setLogin("user1");
        user1.setName("User One");
        user1.setBirthday(LocalDate.of(1990, 1, 1));
        User createdUser1 = userController.createUser(user1);

        User user2 = new User();
        user2.setEmail("user2@example.com");
        user2.setLogin("user2");
        user2.setName("User Two");
        user2.setBirthday(LocalDate.of(1992, 2, 2));
        User createdUser2 = userController.createUser(user2);

        userController.addFriends(createdUser1.getId(), createdUser2.getId());

        List<User> friendsList = userController.getFriendsListOfPerson(createdUser1.getId());

        assertEquals(1, friendsList.size());
        assertEquals("user2", friendsList.get(0).getLogin());
    }

    @Test
    void getListOfCommonFriendsShouldReturnCommonFriends() {
        User user1 = new User();
        user1.setEmail("user1@example.com");
        user1.setLogin("user1");
        user1.setName("User One");
        user1.setBirthday(LocalDate.of(1990, 1, 1));
        User createdUser1 = userController.createUser(user1);

        User user2 = new User();
        user2.setEmail("user2@example.com");
        user2.setLogin("user2");
        user2.setName("User Two");
        user2.setBirthday(LocalDate.of(1992, 2, 2));
        User createdUser2 = userController.createUser(user2);

        User user3 = new User();
        user3.setEmail("user3@example.com");
        user3.setLogin("user3");
        user3.setName("User Three");
        user3.setBirthday(LocalDate.of(1995, 5, 5));
        User createdUser3 = userController.createUser(user3);

        userController.addFriends(createdUser1.getId(), createdUser3.getId());
        userController.addFriends(createdUser2.getId(), createdUser3.getId());

        List<User> commonFriends = userController.getListOfCommonFriends(createdUser1.getId(), createdUser2.getId());

        assertEquals(1, commonFriends.size());
        assertEquals("user3", commonFriends.get(0).getLogin());
    }
}
