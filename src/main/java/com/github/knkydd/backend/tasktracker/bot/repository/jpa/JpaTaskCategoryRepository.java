package com.github.knkydd.backend.tasktracker.bot.repository.jpa;

import com.github.knkydd.backend.tasktracker.bot.model.TaskCategory;
import com.github.knkydd.backend.tasktracker.bot.repository.TaskCategoryRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaTaskCategoryRepository extends TaskCategoryRepository, JpaRepository<TaskCategory, Long> {
}
