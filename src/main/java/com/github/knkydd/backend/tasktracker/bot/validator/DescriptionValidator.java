package com.github.knkydd.backend.tasktracker.bot.validator;

import org.springframework.stereotype.Component;

@Component
public class DescriptionValidator {

    public void checkValidated(String description) {
        checkDescriptionOnCommand(description);
    }

    private void checkDescriptionOnCommand(String description) {
        if (description.startsWith("/")) {
            throw new IllegalArgumentException(String.format("Описание задачи: %s является командой", description.split(" ")[0]));
        }
    }
}
