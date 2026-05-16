package com.github.knkydd.backend.tasktracker.core.integration.jdbc;

import com.github.knkydd.backend.tasktracker.core.integration.AbstractDatabaseIntegrationTest;
import com.github.knkydd.backend.tasktracker.core.model.User;
import com.github.knkydd.backend.tasktracker.core.repository.jdbc.JdbcUserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class JdbcUserRepositoryTest extends AbstractDatabaseIntegrationTest {

    @Autowired
    private JdbcUserRepository userRepository;

    @Test
    @DisplayName("Проверка корректности сохранения в бд")
    void test(){
        User user = new User(123);

        User savedUser = userRepository.saveAndFlush(user);

        Assertions.assertEquals(savedUser, userRepository.findById(123).get());
    }

}
