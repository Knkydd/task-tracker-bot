package com.github.knkydd.backend.tasktracker.core.service;

import com.github.knkydd.backend.tasktracker.core.exception.ServerException;
import com.github.knkydd.backend.tasktracker.core.model.TaskCategory;
import com.github.knkydd.backend.tasktracker.core.repository.TaskCategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class TaskCategoryService {

    private final TaskCategoryRepository taskCategoryRepository;

    @Transactional
    public TaskCategory getOrCreateCategory(TaskCategory category) {
        try {
            String name = category.getName();
            Optional<TaskCategory> taskCategory = taskCategoryRepository.findByName(name);
            return taskCategory.orElseGet(() -> taskCategoryRepository.saveAndFlush(new TaskCategory(name)));

        } catch (DataAccessException e) {
            throw new ServerException("Возникла ошибка создания или сохранения категории" + e.getMessage());
        }
    }
}
