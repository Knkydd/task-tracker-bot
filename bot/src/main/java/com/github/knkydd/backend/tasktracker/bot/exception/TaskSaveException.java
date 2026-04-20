package com.github.knkydd.backend.tasktracker.bot.exception;

public class TaskSaveException extends RuntimeException {
    public TaskSaveException(String message) {
        super(message);
    }
}
