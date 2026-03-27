package com.github.knkydd.backend.tasktracker.bot.repository.jpa;

import com.github.knkydd.backend.tasktracker.bot.model.Task;
import com.github.knkydd.backend.tasktracker.bot.repository.TaskRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaTaskRepository extends TaskRepository, JpaRepository<Task, Long> {
}
