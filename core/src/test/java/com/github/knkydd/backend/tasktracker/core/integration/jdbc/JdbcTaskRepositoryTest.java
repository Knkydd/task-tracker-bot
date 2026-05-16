package com.github.knkydd.backend.tasktracker.core.integration.jdbc;

import com.github.knkydd.backend.tasktracker.core.integration.AbstractDatabaseIntegrationTest;
import com.github.knkydd.backend.tasktracker.core.model.Task;
import com.github.knkydd.backend.tasktracker.core.model.TaskCategory;
import com.github.knkydd.backend.tasktracker.core.model.User;
import com.github.knkydd.backend.tasktracker.core.repository.jdbc.JdbcTaskRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class JdbcTaskRepositoryTest extends AbstractDatabaseIntegrationTest {

    @Autowired
    private JdbcTaskRepository taskRepository;

    @Test
    @DisplayName("Проверка корректности сохранения в бд")
    void test(){
        Task task = new Task();
        User user = new User(123);
        TaskCategory category = new TaskCategory("категория");

        task.setCategory(category);
        task.setUser(user);
        task.setDescription("описание");

        Task savedTask = taskRepository.saveAndFlush(task);
        Assertions.assertNotNull(savedTask.getTaskId());
    }
}
