package com.github.knkydd.backend.tasktracker.bot.service;

import com.github.knkydd.backend.tasktracker.bot.exception.GetOrCreateCategoryException;
import com.github.knkydd.backend.tasktracker.bot.model.TaskCategory;
import com.github.knkydd.backend.tasktracker.bot.repository.TaskCategoryRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class TaskCategoryService {

    private final TaskCategoryRepository taskCategoryRepository;

    @Transactional
    public Optional<TaskCategory> getOrCreateCategory(String category) {
        try {
            Optional<TaskCategory> taskCategory = taskCategoryRepository.findByName(category);
            if (taskCategory.isPresent()) {
                return taskCategory;
            }
            return Optional.ofNullable(taskCategoryRepository.saveAndFlush(new TaskCategory(category)));

        } catch (DataAccessException e) {
            throw new GetOrCreateCategoryException("Возникла ошибка создания или сохранения" + e.getMessage());
        }
    }
}
