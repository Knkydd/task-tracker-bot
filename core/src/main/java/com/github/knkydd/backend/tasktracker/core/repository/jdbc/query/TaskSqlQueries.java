package com.github.knkydd.backend.tasktracker.core.repository.jdbc.query;

public class TaskSqlQueries {

    public static final String SQL_SAVE_TASK = "INSERT INTO tasks (chat_id, category_id, description) VALUES (:chatId, :categoryId, :description) RETURNING task_id";
    public static final String SQL_EXISTS_TASK = "SELECT task_id, chat_id, category_id, description FROM tasks WHERE task_id = :taskId AND chat_id = :chatId";
    public static final String SQL_DELETE_TASK = "DELETE FROM tasks WHERE task_id = :taskId AND chat_id = :chatId";
    public static final String SQL_FIND_TASKS = """
            SELECT t.task_id, t.chat_id, c.name, t.description \s
            FROM tasks as t JOIN categories as c ON c.category_id = t.category_id \s
            WHERE chat_id = :chatId
          """;

}
