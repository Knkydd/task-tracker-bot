package com.github.knkydd.backend.tasktracker.core.repository.jpa;

import com.github.knkydd.backend.tasktracker.core.model.Task;
import com.github.knkydd.backend.tasktracker.core.repository.TaskRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

@Profile("jpa")
public interface JpaTaskRepository extends TaskRepository, JpaRepository<Task, Long> {
}
