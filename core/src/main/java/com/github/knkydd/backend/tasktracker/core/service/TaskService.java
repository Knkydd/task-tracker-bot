package com.github.knkydd.backend.tasktracker.core.service;

import com.github.knkydd.backend.tasktracker.core.exception.ServerException;
import com.github.knkydd.backend.tasktracker.core.exception.TaskNotFoundException;
import com.github.knkydd.backend.tasktracker.core.model.Task;
import com.github.knkydd.backend.tasktracker.core.model.TaskCategory;
import com.github.knkydd.backend.tasktracker.core.model.User;
import com.github.knkydd.backend.tasktracker.core.repository.TaskRepository;
import com.github.knkydd.backend.tasktracker.core.web.requests.TaskRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    private final UserService userService;

    private final TaskCategoryService taskCategoryService;

    @Transactional
    public void deleteTask(long taskId, long chatId) {
        try {
            log.info("Проверка существования задачи");
            checkTaskExists(taskId, chatId);
            log.info("Попытка удаления задачи...");
            taskRepository.deleteByTaskIdAndUserChatId(taskId, chatId);
            log.info("Удаление успешно");
        } catch (DataAccessException e) {
            throw new ServerException("Возникла ошибка удаления задачи: " + e.getMessage());
        }
    }

    @Transactional
    public void saveTask(long chatId, TaskRequestDto task) {
        try {
            log.info("Попытка сохранения пользователя");
            User user = userService.getOrCreateUser(chatId);
            log.info("Попытка сохранения категории");
            TaskCategory category = taskCategoryService.getOrCreateCategory(new TaskCategory(task.category()));
            Task newTask = new Task(user, category, task.description());
            log.info("Попытка сохранения задачи");
            taskRepository.saveAndFlush(newTask);
            log.info("Задача успешно сохранена");
        } catch (DataAccessException e) {
            throw new ServerException("Возникла ошибка сохранения задачи. " + e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public List<Task> getUserTasks(long chatId) {
        try {
            log.info("Попытка получить задачи пользователя");
            List<Task> tasks = taskRepository.findAllByUserChatId(chatId);
            if (tasks == null || tasks.isEmpty()) {
                log.info("Задач не обнаружено");
                return Collections.<Task>emptyList();
            }
            log.info("Задачи были найдены");
            return tasks;
        } catch (DataAccessException e) {
            throw new ServerException("Возникла ошибка получения списка задач пользователя " + chatId);
        }
    }

    public void checkTaskExists(long taskId, long chatId) {
        if (!taskRepository.existsByTaskIdAndUserChatId(taskId, chatId)) {
            throw new TaskNotFoundException(String.format("Задачи с номером %s не существует.", taskId));
        }
    }
}
