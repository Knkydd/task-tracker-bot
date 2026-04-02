package com.github.knkydd.backend.tasktracker.bot.service;

import com.github.knkydd.backend.tasktracker.bot.exception.DeleteTaskException;
import com.github.knkydd.backend.tasktracker.bot.exception.SaveTaskException;
import com.github.knkydd.backend.tasktracker.bot.model.Task;
import com.github.knkydd.backend.tasktracker.bot.repository.TaskRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    @Transactional
    public void deleteByTaskIdAndChatId(long taskId, long chatId) {
        try {
            Optional<Task> existsTask = taskRepository.findByTaskIdAndUserChatId(taskId ,chatId);
            if (existsTask.isEmpty()) {
                throw new DeleteTaskException("Такой задачи не существует");
            }
            taskRepository.deleteByTaskIdAndUserChatId(taskId, chatId);
        } catch (DataAccessException e) {
            throw new DeleteTaskException("Возникла ошибка удаления задачи " + e.getMessage());
        }
    }

    @Transactional
    public void saveAndFlush(Task task) {
        try {
            taskRepository.saveAndFlush(task);
        } catch (DataAccessException e) {
            throw new SaveTaskException("Возникла ошибка сохранения задачи. " + e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public List<Task> findAllByChatId(long chatId) {
        try {
            return taskRepository.findAllByUserChatId(chatId);
        } catch (DataAccessException e) {
            log.error("Возникла ошибка при поиске по chatId. {}", e.getMessage());
            return Collections.<Task>emptyList();
        }
    }

    @Transactional(readOnly = true)
    public void existsTaskById(long taskId) {
        try {
            taskRepository.findByTaskId(taskId);
        } catch (DataAccessException e) {
            log.error("Возникла ошибка при работе с базой данных при поиске по taskId. {}", e.getMessage());
        } catch (Exception e) {
            log.error("Возникла неизвестная ошибка при поиске по taskId. {}", e.getMessage());
        }
    }

}
