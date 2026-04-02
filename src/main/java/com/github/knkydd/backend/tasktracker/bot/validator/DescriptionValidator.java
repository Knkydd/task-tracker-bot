package com.github.knkydd.backend.tasktracker.bot.validator;

import org.springframework.stereotype.Component;

@Component
public class DescriptionValidator {

    public void isValidated(String description) {
        isNotACommand(description);
    }

    private void isNotACommand(String description) {
        if (description.startsWith("/")) {
            throw new IllegalArgumentException(String.format("Описание задачи: %s является командой", description.split(" ")[0]));
        }
    }
}
