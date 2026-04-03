package com.github.knkydd.backend.tasktracker.bot.repository;

import com.github.knkydd.backend.tasktracker.bot.model.Task;

import java.util.List;

public interface TaskRepository {

    List<Task> findAllByUserChatId(long chatId);

    boolean existsByTaskIdAndUserChatId(long taskId, long chatId);

    Task saveAndFlush(Task task);

    void deleteByTaskIdAndUserChatId(long taskId, long chatId);
}
