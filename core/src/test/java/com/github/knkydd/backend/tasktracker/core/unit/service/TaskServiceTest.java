package com.github.knkydd.backend.tasktracker.core.unit.service;

import com.github.knkydd.backend.tasktracker.core.exception.ServerException;
import com.github.knkydd.backend.tasktracker.core.exception.TaskNotFoundException;
import com.github.knkydd.backend.tasktracker.core.model.Task;
import com.github.knkydd.backend.tasktracker.core.model.TaskCategory;
import com.github.knkydd.backend.tasktracker.core.model.User;
import com.github.knkydd.backend.tasktracker.core.repository.TaskRepository;
import com.github.knkydd.backend.tasktracker.core.service.TaskCategoryService;
import com.github.knkydd.backend.tasktracker.core.service.TaskService;
import com.github.knkydd.backend.tasktracker.core.service.UserService;
import com.github.knkydd.backend.tasktracker.core.web.requests.TaskRequestDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    private static final long CHAT_ID = 123;
    private static final long TASK_ID = 1;
    private static final String CATEGORY_NAME = "Работа";
    private static final String DESCRIPTION = "Пойти в зал в 17.00";

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserService userService;

    @Mock
    private TaskCategoryService taskCategoryService;

    private TaskService taskService;

    private final User USER = new User(CHAT_ID);

    private final TaskCategory TASK_CATEGORY = new TaskCategory(CATEGORY_NAME);

    private final Task TASK = new Task(USER, TASK_CATEGORY, DESCRIPTION);

    @BeforeEach
    void setUp() {
        taskService = new TaskService(taskRepository, userService, taskCategoryService);
    }

    @Test
    @DisplayName("Проверка deleteTask с корректными входными данными")
    void deleteTaskDeletesExistingTask() {
        when(taskRepository.existsByTaskIdAndUserChatId(TASK_ID, CHAT_ID)).thenReturn(true);
        when(taskRepository.deleteByTaskIdAndUserChatId(TASK_ID, CHAT_ID)).thenReturn(1L);

        Assertions.assertDoesNotThrow(() -> taskService.deleteTask(TASK_ID, CHAT_ID));
    }

    @Test
    @DisplayName("Проверка deleteTask удаление несуществующей задачи")
    void deleteTaskThrowsWhenTaskDoesNotExist() {
        when(taskRepository.existsByTaskIdAndUserChatId(TASK_ID, CHAT_ID)).thenReturn(false);

        Assertions.assertThrows(TaskNotFoundException.class, () -> taskService.deleteTask(TASK_ID, CHAT_ID));
    }

    @Test
    @DisplayName("Проверка deleteTask удаление задачи другим пользователем")
    void deleteTaskDoesNotDeleteAnotherUserTask() {
        long anotherChatId = 456;
        when(taskRepository.existsByTaskIdAndUserChatId(TASK_ID, anotherChatId)).thenReturn(false);

        Assertions.assertThrows(TaskNotFoundException.class, () -> taskService.deleteTask(TASK_ID, anotherChatId));
    }

    @Test
    @DisplayName("Проверка deleteTask на возникновение ошибки бд")
    void deleteTaskWrapsRepositoryError() {
        when(taskRepository.existsByTaskIdAndUserChatId(TASK_ID, CHAT_ID)).thenReturn(true);
        when(taskRepository.deleteByTaskIdAndUserChatId(TASK_ID, CHAT_ID)).thenThrow(new DataAccessException("db error") {});

        Assertions.assertThrows(ServerException.class, () -> taskService.deleteTask(TASK_ID, CHAT_ID));
    }

    @Test
    @DisplayName("Проверка saveTask")
    void saveTaskCreatesTaskWithUserAndCategory() {
        User user = new User(CHAT_ID);
        TaskCategory category = new TaskCategory(CATEGORY_NAME);
        TaskRequestDto request = new TaskRequestDto(CATEGORY_NAME, DESCRIPTION);
        Task[] savedTask = new Task[1];

        when(userService.getOrCreateUser(CHAT_ID)).thenReturn(user);
        when(taskCategoryService.getOrCreateCategory(any(TaskCategory.class))).thenAnswer(invocation -> {
            TaskCategory taskCategory = invocation.getArgument(0);
            Assertions.assertEquals(CATEGORY_NAME, taskCategory.getName());
            return category;
        });
        when(taskRepository.saveAndFlush(any(Task.class))).thenAnswer(invocation -> {
            savedTask[0] = invocation.getArgument(0);
            return savedTask[0];
        });

        Assertions.assertDoesNotThrow(() -> taskService.saveTask(CHAT_ID, request));

        Assertions.assertNotNull(savedTask[0]);
        Assertions.assertEquals(user, savedTask[0].getUser());
        Assertions.assertEquals(category, savedTask[0].getCategory());
        Assertions.assertEquals(DESCRIPTION, savedTask[0].getDescription());
    }

    @Test
    @DisplayName("Проверка saveTask на возникновение ошибки бд")
    void saveTaskWrapsRepositoryError() {
        TaskRequestDto request = new TaskRequestDto(CATEGORY_NAME, DESCRIPTION);

        when(userService.getOrCreateUser(CHAT_ID)).thenReturn(USER);
        when(taskCategoryService.getOrCreateCategory(TASK_CATEGORY)).thenReturn(TASK_CATEGORY);
        when(taskRepository.saveAndFlush(TASK)).thenThrow(new DataAccessException("db error") {});

        Assertions.assertThrows(ServerException.class, () -> taskService.saveTask(CHAT_ID, request));
    }

    @Test
    @DisplayName("Проверка getUserTasks на получение задач существующего пользователя")
    void getUserTasksReturnsTasks() {
        List<Task> tasks = List.of(TASK);
        when(taskRepository.findAllByUserChatId(CHAT_ID)).thenReturn(tasks);

        Assertions.assertEquals(tasks, taskService.getUserTasks(CHAT_ID));
    }

    @Test
    @DisplayName("Проверка getUserTasks на получение задач существующим пользователем, когда у пользователя нет задач")
    void getUserTasksReturnsEmptyListWhenRepositoryReturnsEmptyList() {
        when(taskRepository.findAllByUserChatId(CHAT_ID)).thenReturn(Collections.emptyList());

        Assertions.assertTrue(taskService.getUserTasks(CHAT_ID).isEmpty());
    }

    @Test
    @DisplayName("Проверка getUserTasks на получение задач несуществующего пользователя")
    void getUserTasksReturnsEmptyListWhenRepositoryReturnsNull() {
        when(taskRepository.findAllByUserChatId(CHAT_ID)).thenReturn(null);

        Assertions.assertTrue(taskService.getUserTasks(CHAT_ID).isEmpty());
    }

    @Test
    @DisplayName("Проверка getUserTasks на возникновение ошибки бд")
    void getUserTasksWrapsRepositoryError() {
        when(taskRepository.findAllByUserChatId(CHAT_ID)).thenThrow(new DataAccessException("db error") {
        });

        Assertions.assertThrows(ServerException.class, () -> taskService.getUserTasks(CHAT_ID));
    }

    @Test
    @DisplayName("Проверка checkTaskExists с существующей задачей")
    void checkTaskExistsDoesNothingWhenTaskExists() {
        when(taskRepository.existsByTaskIdAndUserChatId(TASK_ID, CHAT_ID)).thenReturn(true);

        Assertions.assertDoesNotThrow(() -> taskService.checkTaskExists(TASK_ID, CHAT_ID));
    }

    @Test
    @DisplayName("Проверка checkTaskExists с несуществующей задачей")
    void checkTaskExistsThrowsWhenTaskDoesNotExist() {
        when(taskRepository.existsByTaskIdAndUserChatId(TASK_ID, CHAT_ID)).thenReturn(false);

        Assertions.assertThrows(TaskNotFoundException.class, () -> taskService.checkTaskExists(TASK_ID, CHAT_ID));
    }
}
