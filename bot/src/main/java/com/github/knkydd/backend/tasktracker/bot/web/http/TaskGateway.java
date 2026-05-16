package com.github.knkydd.backend.tasktracker.bot.web.http;

import com.github.knkydd.backend.tasktracker.bot.exception.TaskDeleteException;
import com.github.knkydd.backend.tasktracker.bot.exception.TaskGetException;
import com.github.knkydd.backend.tasktracker.bot.exception.TaskNotFoundException;
import com.github.knkydd.backend.tasktracker.bot.exception.TaskSaveException;
import com.github.knkydd.backend.tasktracker.bot.web.TaskAPI;
import com.github.knkydd.backend.tasktracker.bot.web.requests.CreateTaskRequestDto;
import com.github.knkydd.backend.tasktracker.bot.web.responses.TaskResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskGateway {

    private final TaskAPI taskAPI;

    public List<TaskResponseDto> getTasks(long chatId) {
        try {
            log.info("Попытка получить список задач пользователем {} ...", chatId);
            return taskAPI.getTasks(chatId);
        } catch (RestClientResponseException e) {
            if (e.getStatusCode().value() == 500) {
                log.error("Сервер вернул 500 ошибку на запрос списка задач пользователя {}. {}", chatId, e.getMessage());
                throw new TaskGetException(e.getMessage());
            }
        } catch (Exception e){
            log.error("Возникла неизвестная ошибка. {}", e.getMessage());
            throw new RuntimeException("Возникла неизвестная ошибка. " + e.getMessage());
        }
        return Collections.emptyList();
    }

    public void saveTask(long chatId, CreateTaskRequestDto task) {
        try {
            log.info("Попытка сохранить задачу {} пользователем {}", task, chatId);
            taskAPI.saveTask(chatId, task);
        } catch (RestClientResponseException e) {
            if (e.getStatusCode().value() == 500) {
                log.error("Сервер вернул 500 ошибку на запрос сохранения задачи пользователем {}. {}", chatId, e.getMessage());
                throw new TaskSaveException(e.getMessage());
            }
        } catch (Exception e){
            log.error("Возникла неизвестная ошибка. {}", e.getMessage());
            throw new RuntimeException("Возникла неизвестная ошибка. " + e.getMessage());
        }
    }

    public void deleteTask(long chatId, long taskId) {
        try {
            taskAPI.deleteTask(chatId, taskId);
        } catch (RestClientResponseException e) {
            if (e.getStatusCode().value() == 500) {
                log.error("Сервер вернул 500 ошибку на запрос удаления задачи пользователем {}. {}", chatId, e.getMessage());
                throw new TaskDeleteException(e.getMessage());
            } else if (e.getStatusCode().value() == 404) {
                log.error("Сервер вернул 404 ошибку на запрос удаления задачи пользователем {}. {}", chatId, e.getMessage());
                throw new TaskNotFoundException(e.getMessage());
            }
        } catch (Exception e){
            log.error("Возникла неизвестная ошибка. {}", e.getMessage());
            throw new RuntimeException("Возникла неизвестная ошибка. " + e.getMessage());
        }
    }
}
