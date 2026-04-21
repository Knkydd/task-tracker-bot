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
            log.info("Попытка получить категорию {} ...", name);
            Optional<TaskCategory> taskCategory = taskCategoryRepository.findByName(name);
            if (taskCategory.isPresent()) {
                log.info("Категория {} найдена.", name);
                return taskCategory.get();
            } else {
                log.info("Категория {} не найдена. Попытка сохранения...", name);
                return taskCategoryRepository.saveAndFlush(category);
            }
        } catch (DataAccessException e) {
            throw new ServerException("Возникла ошибка создания или сохранения категории" + e.getMessage());
        }
    }
}
