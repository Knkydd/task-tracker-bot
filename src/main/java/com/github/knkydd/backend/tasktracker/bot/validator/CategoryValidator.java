package com.github.knkydd.backend.tasktracker.bot.validator;

import lombok.experimental.UtilityClass;

@UtilityClass
public class CategoryValidator {

    public static void checkValidated(String category) {
        checkCategoryOnCommand(category);
    }

    private static void checkCategoryOnCommand(String category) {
        if (category.startsWith("/")) {
            throw new IllegalArgumentException(String.format("Категория: %s является командой", category));
        }
    }
}
