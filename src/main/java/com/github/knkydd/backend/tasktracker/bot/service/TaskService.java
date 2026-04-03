package com.github.knkydd.backend.tasktracker.bot.service;

import com.github.knkydd.backend.tasktracker.bot.exception.DeleteTaskException;
import com.github.knkydd.backend.tasktracker.bot.exception.NoSuchTaskInRepositoryException;
import com.github.knkydd.backend.tasktracker.bot.exception.SaveTaskException;
import com.github.knkydd.backend.tasktracker.bot.model.Task;
import com.github.knkydd.backend.tasktracker.bot.repository.TaskRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    @Transactional
    public void deleteByTaskIdAndChatId(long taskId, long chatId) {
        try {
            checkTaskExists(taskId, chatId);
            taskRepository.deleteByTaskIdAndUserChatId(taskId, chatId);
        } catch (NoSuchTaskInRepositoryException | DataAccessException e) {
            throw new DeleteTaskException("Возникла ошибка удаления задачи: " + e.getMessage());
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
            throw new NoSuchTaskInRepositoryException("Возникла ошибка получения списка задач пользователя " + chatId);
        }
    }

    public void checkTaskExists(long taskId, long chatId) {
        if (!taskRepository.existsByTaskIdAndUserChatId(taskId, chatId)) {
            throw new NoSuchTaskInRepositoryException(String.format("Задачи с номером %s не существует.", taskId));
        }
    }
}
