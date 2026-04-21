package com.github.knkydd.backend.tasktracker.core.web.controller;

import com.github.knkydd.backend.tasktracker.core.exception.*;
import com.github.knkydd.backend.tasktracker.core.web.responses.ExceptionControllerResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler({
            ServerException.class,
            Exception.class
    })
    public ResponseEntity<ExceptionControllerResponse> handleServerException(Exception e) {
        log.error("{}. {}", e.getClass(), e.getMessage());
        var response = new ExceptionControllerResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<ExceptionControllerResponse> handleTaskNotFound(TaskNotFoundException e) {
        log.error("{}. {}", e.getClass(), e.getMessage());
        var response = new ExceptionControllerResponse(
                HttpStatus.NOT_FOUND.value(), e.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
}
