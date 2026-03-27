package com.github.knkydd.backend.tasktracker.bot.repository;

import com.github.knkydd.backend.tasktracker.bot.model.TaskCategory;

import java.util.List;
import java.util.Optional;

public interface TaskCategoryRepository {
    List<TaskCategory> findAll();

    Optional<TaskCategory> findByName(String categoryName);

    TaskCategory saveAndFlush(TaskCategory category);

    void deleteById(long categoryId);

    void deleteByName(String categoryName);
}
