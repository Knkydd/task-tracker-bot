package com.github.knkydd.backend.tasktracker.bot.validator;

import lombok.experimental.UtilityClass;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@UtilityClass
public class DescriptionValidator {

    public static void checkValidated(String description) {
        checkDescriptionOnNull(description);
        checkDescriptionOnNumber(description);
        checkDescriptionOnCommand(description);
    }

    private static void checkDescriptionOnCommand(String description) {
        if (description.startsWith("/")) {
            throw new IllegalArgumentException(String.format("Описание задачи: %s является командой", description.split(" ")[0]));
        }
    }

    private static void checkDescriptionOnNull(String category) {
        if (category == null) {
            throw new IllegalArgumentException("Описание задачи является null");
        }
    }

    private static void checkDescriptionOnNumber(String category) {
        String pattern = "[0-9]+";
        Pattern pt = Pattern.compile(pattern);
        Matcher matcher = pt.matcher(category);
        if (matcher.find()) {
            throw new IllegalArgumentException(String.format("В переданном описании задачи %s есть числа", category));
        }
    }
}
