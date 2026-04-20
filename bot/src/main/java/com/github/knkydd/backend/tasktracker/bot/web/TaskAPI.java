package com.github.knkydd.backend.tasktracker.bot.web;

import com.github.knkydd.backend.tasktracker.bot.web.requests.CreateTaskRequestDto;
import com.github.knkydd.backend.tasktracker.bot.web.responses.TaskResponseDto;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.DeleteExchange;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

import java.util.List;

@HttpExchange("/tasks")
public interface TaskAPI {
    @GetExchange("/{chatId}")
    List<TaskResponseDto> getTasks(@PathVariable long chatId);

    @PostExchange("/{chatId}")
    void saveTask(@PathVariable long chatId,
                  @RequestBody CreateTaskRequestDto task);

    @DeleteExchange("/{chatId}/{taskId}")
    void deleteTask(@PathVariable long chatId,
                    @PathVariable long taskId);
}
