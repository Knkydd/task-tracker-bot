package com.github.knkydd.backend.tasktracker.core.repository.jdbc.query;

public class TaskCategorySqlQueries {

    public static final String SQL_FIND_TASK_CATEGORY = "SELECT 1 FROM categories WHERE name = :categoryName";
    public static final String SQL_SAVE_TASK_CATEGORY = """
             INSERT INTO categories(name) VALUES(:categoryName) \s
             RETURNING category_id
            """;

}
