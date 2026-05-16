package com.github.knkydd.backend.tasktracker.core.unit.web;

import com.github.knkydd.backend.tasktracker.core.exception.ServerException;
import com.github.knkydd.backend.tasktracker.core.exception.TaskNotFoundException;
import com.github.knkydd.backend.tasktracker.core.model.Task;
import com.github.knkydd.backend.tasktracker.core.model.TaskCategory;
import com.github.knkydd.backend.tasktracker.core.model.User;
import com.github.knkydd.backend.tasktracker.core.service.TaskService;
import com.github.knkydd.backend.tasktracker.core.web.controller.TaskController;
import com.github.knkydd.backend.tasktracker.core.web.requests.TaskRequestDto;
import com.github.knkydd.backend.tasktracker.core.web.responses.TaskResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.org.checkerframework.checker.units.qual.C;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.mockito.Mockito.when;

@WebMvcTest(TaskController.class)
public class TaskControllerTest {

    private final long CHAT_ID = 123;

    private final long TASK_ID = 1;

    private final String CATEGORY = "Категория";

    private final TaskCategory TASK_CATEGORY = new TaskCategory(CATEGORY);

    private final User USER = new User(CHAT_ID);

    private final String DESCRIPTION = "Описание";

    private final Task TASK = new Task(TASK_ID, USER, TASK_CATEGORY, DESCRIPTION);

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TaskService taskService;

    @Test
    @DisplayName("Проверка getTask с корректными входными данными")
    void getTasks() throws Exception {
        List<Task> tasks = List.of(TASK);
        when(taskService.getUserTasks(CHAT_ID)).thenReturn(tasks);

        mockMvc.perform(get("/tasks/{chatId}", CHAT_ID).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].taskId").value(TASK_ID))
                .andExpect(jsonPath("$[0].category").value(CATEGORY))
                .andExpect(jsonPath("$[0].description").value(DESCRIPTION));
    }

    @Test
    @DisplayName("Проверка getTasks на ошибку")
    void getTasksWithError() throws Exception {
        when(taskService.getUserTasks(CHAT_ID)).thenThrow(new ServerException("Ошибка"));

        mockMvc.perform(get("/tasks/{chatId}", CHAT_ID)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError());
    }

    @Test
    @DisplayName("Проверка saveTask с корректными входными данными")
    void saveTaskWithCorrectData() throws Exception {
        String json = String.format("""
                    {
                        "category": "%s",
                        "description": "%s"
                    }
                """, CATEGORY, DESCRIPTION);
        TaskRequestDto requestDto = new TaskRequestDto(CATEGORY, DESCRIPTION);
        when(taskService.saveTask(CHAT_ID, requestDto)).thenReturn(1L);

        mockMvc.perform(post("/tasks/{chatId}", CHAT_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @DisplayName("Проверка saveTask на ошибку")
    void saveTaskOnError() throws Exception {
        TaskRequestDto requestDto = new TaskRequestDto(CATEGORY, DESCRIPTION);
        when(taskService.saveTask(CHAT_ID, requestDto)).thenThrow(new ServerException("Ошибка"));

        mockMvc.perform(post("/tasks/{chatId}", CHAT_ID)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError());
    }

    @Test
    @DisplayName("Проверка deleteTask с корректными входными данными")
    void deleteTaskWithCorrectData() throws Exception {
        when(taskService.deleteTask(TASK_ID, CHAT_ID)).thenReturn(1L);

        mockMvc.perform(delete("/tasks/{chatId}/{taskId}", CHAT_ID, TASK_ID)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Проверка deleteTask на возникновение ошибки")
    void deleteTaskOnError() throws Exception {
        when(taskService.deleteTask(TASK_ID, CHAT_ID)).thenThrow(new ServerException("Ошибка"));

        mockMvc.perform(delete("/tasks/{chatId}/{taskId}", CHAT_ID, TASK_ID)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError());
    }

    @Test
    @DisplayName("Проверка deleteTask на удаление задачи которая пользователю не принадлежит")
    void deleteTaskOn404Error() throws Exception {
        when(taskService.deleteTask(TASK_ID, CHAT_ID)).thenThrow(new TaskNotFoundException("Ошибка"));

        mockMvc.perform(delete("/tasks/{chatId}/{taskId}", CHAT_ID, TASK_ID)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

    }
}
