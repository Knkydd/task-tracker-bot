package com.github.knkydd.backend.tasktracker.core.repository.jdbc.query;

public class UserSqlQueries {

    public static final String SQL_SAVE_USER = "INSERT INTO users(chat_id) VALUES(:chatId)";
    public static final String SQL_FIND_USER = "SELECT 1 FROM users WHERE chat_id = :chatId";

}
