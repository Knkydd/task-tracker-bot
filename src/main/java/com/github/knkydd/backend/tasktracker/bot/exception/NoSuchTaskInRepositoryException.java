package com.github.knkydd.backend.tasktracker.bot.exception;

public class NoSuchTaskInRepositoryException extends RuntimeException {
    public NoSuchTaskInRepositoryException(String message) {
        super(message);
    }
}
