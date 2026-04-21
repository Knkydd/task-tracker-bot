package com.github.knkydd.backend.tasktracker.core.repository.jpa;

import com.github.knkydd.backend.tasktracker.core.model.TaskCategory;
import com.github.knkydd.backend.tasktracker.core.repository.TaskCategoryRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaTaskCategoryRepository extends TaskCategoryRepository, JpaRepository<TaskCategory, Long> {
}
