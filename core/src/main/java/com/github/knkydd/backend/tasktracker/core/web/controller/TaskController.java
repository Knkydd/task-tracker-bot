package com.github.knkydd.backend.tasktracker.core.web.controller;

import com.github.knkydd.backend.tasktracker.core.service.TaskService;
import com.github.knkydd.backend.tasktracker.core.web.responses.TaskResponseDto;
import com.github.knkydd.backend.tasktracker.core.web.requests.TaskRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
@Slf4j
public class TaskController {

    private final TaskService taskService;

    @GetMapping("/{chatId}")
    public ResponseEntity<List<TaskResponseDto>> getTasks(@PathVariable long chatId) {
        log.info("Пришел GET запрос на /tasks/{chatId}");
        List<TaskResponseDto> tasks = taskService.getUserTasks(chatId).stream()
                .map(task -> new TaskResponseDto(
                        task.getTaskId(),
                        task.getCategory().getName(),
                        task.getDescription()
                ))
                .toList();
        log.info("Запрос успешно обработан. Отправка ответа...");
        return ResponseEntity.status(HttpStatus.OK).body(tasks);
    }

    @PostMapping("/{chatId}")
    public ResponseEntity<?> saveTask(@PathVariable long chatId,
                                      @RequestBody TaskRequestDto task) {
        log.info("Пришел POST запрос на /tasks/{chatId}");
        taskService.saveTask(chatId, task);
        log.info("Запрос успешно обработан. Отправка ответа...");
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{chatId}/{taskId}")
    public ResponseEntity<?> deleteTask(@PathVariable long chatId,
                                        @PathVariable long taskId) {
        log.info("Пришел DELETE запрос на /tasks/{chatId}/{taskId}");
        taskService.deleteTask(taskId, chatId);
        log.info("Запрос успешно обработан. Отправка ответа...");
        return ResponseEntity.noContent().build();
    }
}
