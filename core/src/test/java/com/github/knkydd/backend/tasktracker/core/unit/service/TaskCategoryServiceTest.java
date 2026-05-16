package com.github.knkydd.backend.tasktracker.core.unit.service;

import com.github.knkydd.backend.tasktracker.core.exception.ServerException;
import com.github.knkydd.backend.tasktracker.core.model.TaskCategory;
import com.github.knkydd.backend.tasktracker.core.repository.TaskCategoryRepository;
import com.github.knkydd.backend.tasktracker.core.service.TaskCategoryService;
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
public class TaskCategoryServiceTest {

    @Mock
    private TaskCategoryRepository taskCategoryRepository;

    private static final String categoryString = "Категория";

    private static final TaskCategory category = new TaskCategory(categoryString);

    private TaskCategoryService service;

    @BeforeEach
    void setUp() {
        service = new TaskCategoryService(taskCategoryRepository);
    }

    @Test
    @DisplayName("Тест на передачу существующей категории")
    void getOrCreateCategoryReturnsExistingCategory() {
        when(taskCategoryRepository.findByName(categoryString)).thenReturn(Optional.of(category));
        Assertions.assertEquals(category, service.getOrCreateCategory(category));
    }

    @Test
    @DisplayName("Тест на передачу новой категории")
    void getOrCreateCategoryCreatesNewCategory() {
        when(taskCategoryRepository.findByName(categoryString)).thenReturn(Optional.empty());
        when(taskCategoryRepository.saveAndFlush(category)).thenReturn(category);

        Assertions.assertEquals(category, service.getOrCreateCategory(category));
    }

    @Test
    @DisplayName("Тест на проверку возвращение от репозитория null")
    void getOrCreateCategoryThrowsWhenRepositoryReturnsNull() {
        when(taskCategoryRepository.findByName(categoryString)).thenReturn(null);

        Assertions.assertThrows(ServerException.class, () -> service.getOrCreateCategory(category));
    }

    @Test
    @DisplayName("Тест на возникновение ошибки")
    void getOrCreateCategoryThrowsWhenRepositoryFails() {
        when(taskCategoryRepository.findByName(categoryString)).thenThrow(new DataAccessException("Ошибка") {});

        Assertions.assertThrows(ServerException.class, () -> service.getOrCreateCategory(category));
    }

}








