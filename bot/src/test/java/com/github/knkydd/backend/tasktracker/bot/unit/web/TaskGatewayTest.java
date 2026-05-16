package com.github.knkydd.backend.tasktracker.bot.unit.web;

import com.github.knkydd.backend.tasktracker.bot.exception.TaskGetException;
import com.github.knkydd.backend.tasktracker.bot.exception.TaskNotFoundException;
import com.github.knkydd.backend.tasktracker.bot.web.TaskAPI;
import com.github.knkydd.backend.tasktracker.bot.web.http.TaskGateway;
import com.github.knkydd.backend.tasktracker.bot.web.responses.TaskResponseDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClientResponseException;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TaskGatewayTest {

    private final List<TaskResponseDto> tasks = List.of(new TaskResponseDto(1, "категория", "описание"));

    @Mock
    TaskAPI mockTaskAPI;

    @Test
    @DisplayName("Проверка getTasks с правильными параметрами")
    void testGetTasksTrue() {
        when(mockTaskAPI.getTasks(123)).thenReturn(tasks);
        TaskGateway gateway = new TaskGateway(mockTaskAPI);
        Assertions.assertEquals(tasks, gateway.getTasks(123));
    }

    @Test
    @DisplayName("Проверка getTasks на обработку 500 ошибки")
    void testGetTasks500Error() {
        when(mockTaskAPI.getTasks(123)).thenThrow(new RestClientResponseException("", 500, "", null, null, null));
        TaskGateway gateway = new TaskGateway(mockTaskAPI);
        Assertions.assertThrows(TaskGetException.class, () -> gateway.getTasks(123));
    }

    @Test
    @DisplayName("Проверка запроса списка задач, когда пользователь не зарегистрирован")
    void testOnGetTasksByNoAuthUser() {
        when(mockTaskAPI.getTasks(1)).thenReturn(Collections.emptyList());
        TaskGateway gateway = new TaskGateway(mockTaskAPI);
        Assertions.assertEquals(Collections.emptyList(), gateway.getTasks(1));
    }

    @Test
    @DisplayName("Проверка запроса удаления незарегистрированным пользователем")
    void testOnDeleteTaskByNoAuthUser() {
        when(mockTaskAPI.deleteTask(123,1))
                .thenThrow(new RestClientResponseException("", 404, "", null, null, null));
        TaskGateway gateway = new TaskGateway(mockTaskAPI);
        Assertions.assertThrows(TaskNotFoundException.class, () -> gateway.deleteTask(123,1));
    }
}
