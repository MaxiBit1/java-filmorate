package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.UserException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.users.User;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Класс для тестов UserController
 *
 * @author Max Vasilyev
 * @version 1.0
 */
@SpringBootTest
public class UserControllerTest {

    @Autowired
    UserController userController;

    @Test
    public void shouldCreateUser() {
        User user = User.builder()
                .email("mail@mail.ru")
                .name("Jhon")
                .login("Viskas")
                .birthday(LocalDate.of(1946, Month.MAY, 2))
                .build();
        userController.createUser(user);
        assertEquals(user, userController.getUsers().get(4));
    }

    @Test
    public void shouldReturnExceptionEmail() {
        User user = User.builder()
                .email("mailmail.ru")
                .name("Jhon")
                .login("Viskas")
                .birthday(LocalDate.of(1946, Month.MAY, 2))
                .build();
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> userController.createUser(user)
        );
        assertNotNull(exception);
        assertEquals("Ошибка в Email", exception.getMessage());
        ValidationException exception1 = assertThrows(
                ValidationException.class,
                () -> userController.updateUser(user)
        );
        assertNotNull(exception1);
        assertEquals("Ошибка в Email", exception1.getMessage());
        User user1 = User.builder()
                .email(null)
                .name("Jhon")
                .login("Viskas")
                .birthday(LocalDate.of(1946, Month.MAY, 2))
                .build();
        ValidationException exception2 = assertThrows(
                ValidationException.class,
                () -> userController.createUser(user1)
        );
        assertNotNull(exception2);
        assertEquals("Ошибка в Email", exception2.getMessage());
        ValidationException exception3 = assertThrows(
                ValidationException.class,
                () -> userController.updateUser(user1)
        );
        assertNotNull(exception3);
        assertEquals("Ошибка в Email", exception3.getMessage());
    }

    @Test
    public void shouldReturnExceptionLogin() {
        User user = User.builder()
                .email("mail@mail.ru")
                .name("Jhon")
                .login(null)
                .birthday(LocalDate.of(1946, Month.MAY, 2))
                .build();
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> userController.createUser(user)
        );
        assertNotNull(exception);
        assertEquals("Ошибка в логине", exception.getMessage());
        ValidationException exception1 = assertThrows(
                ValidationException.class,
                () -> userController.updateUser(user)
        );
        assertNotNull(exception1);
        assertEquals("Ошибка в логине", exception1.getMessage());
        User user1 = User.builder()
                .email("mail@mail.ru")
                .name("Jhon")
                .login(" ")
                .birthday(LocalDate.of(1946, Month.MAY, 2))
                .build();
        ValidationException exception2 = assertThrows(
                ValidationException.class,
                () -> userController.createUser(user1)
        );
        assertNotNull(exception2);
        assertEquals("Ошибка в логине", exception2.getMessage());
        ValidationException exception3 = assertThrows(
                ValidationException.class,
                () -> userController.updateUser(user1)
        );
        assertNotNull(exception3);
        assertEquals("Ошибка в логине", exception3.getMessage());
    }

    @Test
    public void shouldReturnExceptionBirthday() {
        User user = User.builder()
                .email("mail@mail.ru")
                .name("Jhon")
                .login("VVVVVV")
                .birthday(LocalDate.of(2150, Month.MAY, 2))
                .build();
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> userController.createUser(user)
        );
        assertNotNull(exception);
        assertEquals("Ошибка в дне рождения", exception.getMessage());
        ValidationException exception1 = assertThrows(
                ValidationException.class,
                () -> userController.updateUser(user)
        );
        assertNotNull(exception1);
        assertEquals("Ошибка в дне рождения", exception1.getMessage());
    }

    @Test
    public void shouldSetUpNameLikeLogin() {
        User user = User.builder()
                .email("mail@mail.ru")
                .login("VVVVVV")
                .birthday(LocalDate.of(2001, Month.MAY, 2))
                .build();
        userController.createUser(user);
        assertNotNull(user.getName());
        assertEquals(user.getLogin(), user.getName());
        User user1 = User.builder()
                .name(" ")
                .email("mail@mail.ru")
                .login("VVVVVV")
                .birthday(LocalDate.of(2001, Month.MAY, 2))
                .build();
        userController.createUser(user1);
        assertNotNull(user1.getName());
        assertEquals(user1.getLogin(), user1.getName());
    }

    @Test
    public void shouldReturnExceptionUserExist() {
        User user = User.builder()
                .email("mail@mail.ru")
                .name("Jhon")
                .login("VVVVVV")
                .birthday(LocalDate.of(2012, Month.MAY, 2))
                .build();
        userController.createUser(user);
        User user1 = User.builder()
                .id(1)
                .email("mail@mail.ru")
                .name("Jhon")
                .login("VVVVVV")
                .birthday(LocalDate.of(2012, Month.MAY, 2))
                .build();
        UserException exception = assertThrows(
                UserException.class,
                () -> userController.createUser(user1)
        );
        assertNotNull(exception);
        assertEquals("Такой user уже есть", exception.getMessage());
    }

    @Test
    public void shouldReturnExceptionUserNotExist() {
        User user1 = User.builder()
                .id(99999)
                .email("mail@mail.ru")
                .name("Jhon")
                .login("VVVVVV")
                .birthday(LocalDate.of(2012, Month.MAY, 2))
                .build();
        UserException exception = assertThrows(
                UserException.class,
                () -> userController.updateUser(user1)
        );
        assertNotNull(exception);
        assertEquals("Такого user нет", exception.getMessage());
    }

    @Test
    public void shouldUpdateUser() {
        User user = User.builder()
                .email("mail@mail.ru")
                .name("Jhon")
                .login("VVVVVV")
                .birthday(LocalDate.of(2012, Month.MAY, 2))
                .likesFilmsId(new HashSet<>())
                .friendsId(new HashSet<>())
                .build();
        userController.createUser(user);
        User user1 = User.builder()
                .id(1)
                .email("dasdsadl@mail.ru")
                .name("aaaaaaaaaa")
                .login("VsadsdaV")
                .birthday(LocalDate.of(2012, Month.MAY, 2))
                .likesFilmsId(new HashSet<>())
                .friendsId(new HashSet<>())
                .build();
        userController.updateUser(user1);
        assertEquals(user1, userController.getUsers().get(0));
    }
}
