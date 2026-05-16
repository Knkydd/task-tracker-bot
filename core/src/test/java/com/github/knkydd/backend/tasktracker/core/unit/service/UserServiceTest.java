package com.github.knkydd.backend.tasktracker.core.unit.service;

import com.github.knkydd.backend.tasktracker.core.exception.ServerException;
import com.github.knkydd.backend.tasktracker.core.model.User;
import com.github.knkydd.backend.tasktracker.core.repository.UserRepository;
import com.github.knkydd.backend.tasktracker.core.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;

import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    private static final long CHAT_ID = 123;

    private static final User user = new User(CHAT_ID);

    private UserService service;

    @BeforeEach
    void setUp() {
        service = new UserService(userRepository);
    }

    @Test
    @DisplayName("Тест на возвращение существующего пользователя")
    void getOrCreateUserReturnsExistingUser() {
        when(userRepository.findById(CHAT_ID)).thenReturn(Optional.of(user));

        Assertions.assertEquals(user, service.getOrCreateUser(CHAT_ID));
    }

    @Test
    @DisplayName("Тест на возвращение нового пользователя")
    void getOrCreateUserCreatesNewUser() {
        when(userRepository.findById(CHAT_ID)).thenReturn(Optional.empty());
        when(userRepository.saveAndFlush(user)).thenReturn(user);

        Assertions.assertEquals(user, service.getOrCreateUser(CHAT_ID));
    }

    @Test
    @DisplayName("Тест на ошибку")
    void getOrCreateUserThrowsWhenRepositoryFails() {
        when(userRepository.findById(CHAT_ID)).thenThrow(new DataAccessException("") {
        });

        Assertions.assertThrows(ServerException.class, () -> service.getOrCreateUser(CHAT_ID));
    }

    @Test
    @DisplayName("Тест на получение от репозитория null")
    void getOrCreateUserThrowsWhenRepositoryReturnsNull() {
        when(userRepository.findById(CHAT_ID)).thenReturn(null);

        Assertions.assertThrows(ServerException.class, () -> service.getOrCreateUser(CHAT_ID));
    }
}
