package com.github.knkydd.backend.tasktracker.core.repository.jdbc;

import com.github.knkydd.backend.tasktracker.core.model.Task;
import com.github.knkydd.backend.tasktracker.core.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.github.knkydd.backend.tasktracker.core.repository.jdbc.query.TaskSqlQueries.*;

@Profile("jdbc")
@Repository
@RequiredArgsConstructor
public class JdbcTaskRepository implements TaskRepository {

    private final JdbcClient jdbcClient;

    @Override
    public List<Task> findAllByUserChatId(long chatId) {
        return jdbcClient.sql(SQL_FIND_TASKS)
                .param("chatId", chatId)
                .query(Task.class).list();
    }

    @Override
    public boolean existsByTaskIdAndUserChatId(long taskId, long chatId) {
        Optional<Task> task = jdbcClient.sql(SQL_EXISTS_TASK)
                .param("taskId", taskId)
                .param("chatId", chatId)
                .query(Task.class).optional();

        return task.isPresent();
    }

    @Override
    public Task saveAndFlush(Task task) {
        long chatId = task.getUser().getChatId();
        long categoryId = task.getCategory().getCategoryId();
        String description = task.getDescription();

        long taskId = jdbcClient.sql(SQL_SAVE_TASK)
                .param("chatId", chatId)
                .param("categoryId", categoryId)
                .param("description", description)
                .query(Long.class).single();
        task.setTaskId(taskId);

        return task;
    }

    @Override
    public long deleteByTaskIdAndUserChatId(long taskId, long chatId) {
        return jdbcClient.sql(SQL_DELETE_TASK)
                .param("taskId", taskId)
                .param("chatId", chatId)
                .update();
    }
}
