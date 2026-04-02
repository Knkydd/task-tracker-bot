package com.github.knkydd.backend.tasktracker.bot.validator;

import com.github.knkydd.backend.tasktracker.bot.exception.NoSuchTaskInRepositoryException;
import com.github.knkydd.backend.tasktracker.bot.exception.NotANumberException;
import com.github.knkydd.backend.tasktracker.bot.service.TaskService;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class IdValidator {

    private final TaskService taskService;

    public void isValidated(String maybeNumber) {
        isNumber(maybeNumber);
        isCorrectId(maybeNumber);
    }

    private void isNumber(String maybeNumber) throws NotANumberException {
        try {
            Long number = Long.parseLong(maybeNumber);
        } catch (NumberFormatException e) {
            throw new NotANumberException("Переданная строка не является числом. " + e.getMessage());
        }
    }

    private void isCorrectId(String number) {
        try {
            taskService.existsTaskById(Long.parseLong(number));
        } catch (DataAccessException e) {
            throw new NoSuchTaskInRepositoryException(String.format("Задачи с номером %s не существует ", number) + e.getMessage());
        }
    }
}
