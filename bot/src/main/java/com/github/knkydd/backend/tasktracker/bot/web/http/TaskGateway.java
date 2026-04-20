package com.github.knkydd.backend.tasktracker.bot.web.http;

import com.github.knkydd.backend.tasktracker.bot.exception.TaskDeleteException;
import com.github.knkydd.backend.tasktracker.bot.exception.TaskGetException;
import com.github.knkydd.backend.tasktracker.bot.exception.TaskNotFoundException;
import com.github.knkydd.backend.tasktracker.bot.exception.TaskSaveException;
import com.github.knkydd.backend.tasktracker.bot.web.TaskAPI;
import com.github.knkydd.backend.tasktracker.bot.web.requests.CreateTaskRequestDto;
import com.github.knkydd.backend.tasktracker.bot.web.responses.TaskResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskGateway {

    private final TaskAPI taskAPI;

    public List<TaskResponseDto> getTasks(long chatId) {
        try {
            return taskAPI.getTasks(chatId);
        } catch (RestClientResponseException e) {
            if (e.getStatusCode().value() == 500) {
                throw new TaskGetException(e.getMessage());
            }
        }
        return Collections.emptyList();
    }

    public void saveTask(long chatId, CreateTaskRequestDto task) {
        try {
            taskAPI.saveTask(chatId, task);
        } catch (RestClientResponseException e) {
            if (e.getStatusCode().value() == 500) {
                throw new TaskSaveException(e.getMessage());
            }
        }
    }

    public void deleteTask(long chatId, long taskId) {
        try {
            taskAPI.deleteTask(chatId, taskId);
        } catch (RestClientResponseException e) {
            if (e.getStatusCode().value() == 500) {
                throw new TaskDeleteException(e.getMessage());
            } else if (e.getStatusCode().value() == 404) {
                throw new TaskNotFoundException(e.getMessage());
            }
        }
    }
}
