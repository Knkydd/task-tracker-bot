package com.github.knkydd.backend.tasktracker.bot.unit.web;

import com.github.knkydd.backend.tasktracker.bot.exception.TaskGetException;
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

import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TaskGatewayTest {

    @Mock
    TaskAPI mockTaskAPI;

    @Test
    @DisplayName("Проверка getTasks с правильными параметрами")
    void testGetTasksTrue(){
        List<TaskResponseDto> tasks = List.of(new TaskResponseDto(1,"категория","описание"));
        when(mockTaskAPI.getTasks(123)).thenReturn(tasks);
        TaskGateway gateway = new TaskGateway(mockTaskAPI);
        Assertions.assertEquals(gateway.getTasks(123), tasks);
    }

    @Test
    @DisplayName("Проверка getTasks на 500 ошибку")
    void testGetTasks500Error(){
        when(mockTaskAPI.getTasks(123)).thenThrow(new RestClientResponseException("", 500, "",null,null,null));
        TaskGateway gateway = new TaskGateway(mockTaskAPI);
        Assertions.assertThrows(TaskGetException.class, () -> gateway.getTasks(123));
    }
}
