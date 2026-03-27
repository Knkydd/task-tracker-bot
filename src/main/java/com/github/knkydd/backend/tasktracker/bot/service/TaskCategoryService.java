package com.github.knkydd.backend.tasktracker.bot.service;

import com.github.knkydd.backend.tasktracker.bot.model.TaskCategory;
import com.github.knkydd.backend.tasktracker.bot.repository.TaskCategoryRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class TaskCategoryService {

    private final TaskCategoryRepository taskCategoryRepository;

    @Transactional
    public TaskCategory getOrCreateCategory(String category) {
        Optional<TaskCategory> taskCategory = taskCategoryRepository.findByName(category);
        if (taskCategory.isPresent()) {
            return taskCategory.get();
        }
        try {
            TaskCategory newTackCategory = new TaskCategory();
            newTackCategory.setName(category);
            return taskCategoryRepository.saveAndFlush(newTackCategory);
        } catch (DataIntegrityViolationException e) {
            return taskCategoryRepository.findByName(category).orElseThrow(() -> e);
        }
    }
}
