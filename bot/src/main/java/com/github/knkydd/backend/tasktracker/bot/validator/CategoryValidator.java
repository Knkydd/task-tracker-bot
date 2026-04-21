package com.github.knkydd.backend.tasktracker.bot.validator;

import lombok.experimental.UtilityClass;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@UtilityClass
public class CategoryValidator {

    public static void checkValidated(String category) {
        checkCategoryOnNull(category);
        checkCategoryOnNumber(category);
        checkCategoryOnCommand(category);
    }

    private static void checkCategoryOnCommand(String category) {
        if (category.startsWith("/")) {
            throw new IllegalArgumentException(String.format("Категория %s является командой", category));
        }
    }

    private static void checkCategoryOnNull(String category) {
        if (category == null) {
            throw new IllegalArgumentException("Категория является null");
        }
    }

    private static void checkCategoryOnNumber(String category) {
        String pattern = "[0-9]+";
        Pattern pt = Pattern.compile(pattern);
        Matcher matcher = pt.matcher(category);
        if (matcher.find()) {
            throw new IllegalArgumentException(String.format("В переданной категории %s есть числа", category));
        }
    }
}
