package com.github.knkydd.backend.tasktracker.bot.validator;

import com.github.knkydd.backend.tasktracker.bot.exception.NotANumberException;
import com.github.knkydd.backend.tasktracker.bot.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class IdValidator {

    private final TaskService taskService;

    public void checkValidated(String maybeNumber, long chatId) {
        checkNumber(maybeNumber);
        checkTaskWithIdExists(maybeNumber, chatId);
    }

    private void checkNumber(String maybeNumber) {
        try {
            Long.parseLong(maybeNumber);
        } catch (NumberFormatException e) {
            throw new NotANumberException("Переданная строка не является числом. " + e.getMessage());
        }
    }

    private void checkTaskWithIdExists(String number, long chatId) {
        taskService.checkTaskExists(Long.parseLong(number), chatId);
    }
}
