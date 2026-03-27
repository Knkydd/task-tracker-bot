package com.github.knkydd.backend.tasktracker.bot.repository;

import com.github.knkydd.backend.tasktracker.bot.model.Task;

import java.util.List;
import java.util.Optional;

public interface TaskRepository {

    List<Task> findAllByUserChatId(long chatId);

    Optional<Task> findByTaskId(long id);

    Task saveAndFlush(Task task);

    void deleteByTaskIdAndUserChatId(long taskId, long chatId);
}
