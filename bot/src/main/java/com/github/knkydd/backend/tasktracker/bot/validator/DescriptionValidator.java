package com.github.knkydd.backend.tasktracker.bot.validator;

import lombok.experimental.UtilityClass;

@UtilityClass
public class DescriptionValidator {

    public static void checkValidated(String description) {
        checkDescriptionOnCommand(description);
    }

    private static void checkDescriptionOnCommand(String description) {
        if (description.startsWith("/")) {
            throw new IllegalArgumentException(String.format("Описание задачи: %s является командой", description.split(" ")[0]));
        }
    }
}
