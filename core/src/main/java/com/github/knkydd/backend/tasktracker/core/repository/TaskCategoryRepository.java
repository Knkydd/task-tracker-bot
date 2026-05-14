package com.github.knkydd.backend.tasktracker.core.repository;

import com.github.knkydd.backend.tasktracker.core.model.TaskCategory;

import java.util.Optional;

public interface TaskCategoryRepository {

    Optional<TaskCategory> findByName(String categoryName);

    TaskCategory saveAndFlush(TaskCategory category);

}
