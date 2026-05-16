package com.github.knkydd.backend.tasktracker.core.repository.jdbc;

import com.github.knkydd.backend.tasktracker.core.model.TaskCategory;
import com.github.knkydd.backend.tasktracker.core.repository.TaskCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.github.knkydd.backend.tasktracker.core.repository.jdbc.query.TaskCategorySqlQueries.SQL_FIND_TASK_CATEGORY;
import static com.github.knkydd.backend.tasktracker.core.repository.jdbc.query.TaskCategorySqlQueries.SQL_SAVE_TASK_CATEGORY;

@Profile("jdbc")
@Repository
@RequiredArgsConstructor
public class JdbcTaskCategoryRepository implements TaskCategoryRepository {

    private final JdbcClient jdbcClient;

    @Override
    public Optional<TaskCategory> findByName(String categoryName) {
        return jdbcClient.sql(SQL_FIND_TASK_CATEGORY)
                .param("categoryName", categoryName)
                .query(TaskCategory.class).optional();
    }

    @Override
    public TaskCategory saveAndFlush(TaskCategory category) {
        String categoryName = category.getName();
        long id = jdbcClient.sql(SQL_SAVE_TASK_CATEGORY)
                .param("categoryName", categoryName)
                .query(Long.class).single();
        category.setCategoryId(id);
        return category;
    }
}
