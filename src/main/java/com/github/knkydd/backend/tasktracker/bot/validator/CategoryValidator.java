package com.github.knkydd.backend.tasktracker.bot.validator;

import org.springframework.stereotype.Component;

@Component
public class CategoryValidator {

    public void isValidated(String category) {
        checkCategoryOnCommand(category);
    }

    private void checkCategoryOnCommand(String category) {
        if (category.startsWith("/")) {
            throw new IllegalArgumentException(String.format("Категория: %s является командой", category));
        }
    }
}
