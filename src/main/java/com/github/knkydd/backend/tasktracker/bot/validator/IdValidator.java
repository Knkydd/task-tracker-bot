package com.github.knkydd.backend.tasktracker.bot.validator;

import com.github.knkydd.backend.tasktracker.bot.exception.NotANumberException;
import com.github.knkydd.backend.tasktracker.bot.service.TaskService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class IdValidator {

    private final TaskService taskService;

    public void isValidated(String maybeNumber, long chatId) {
        isNumber(maybeNumber);
        isTaskExists(maybeNumber, chatId);
    }

    private void isNumber(String maybeNumber) {
        try {
            Long.parseLong(maybeNumber);
        } catch (NumberFormatException e) {
            throw new NotANumberException("Переданная строка не является числом. " + e.getMessage());
        }
    }

    private void isTaskExists(String number, long chatId) {
        taskService.checkTaskExists(Long.parseLong(number), chatId);
    }
}
