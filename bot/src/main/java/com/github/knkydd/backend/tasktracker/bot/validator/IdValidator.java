package com.github.knkydd.backend.tasktracker.bot.validator;

import com.github.knkydd.backend.tasktracker.bot.exception.NotANumberException;
import lombok.experimental.UtilityClass;

@UtilityClass
public class IdValidator {

    public static void checkValidated(String maybeNumber) {
        checkOnNumber(maybeNumber);
        checkOnNull(maybeNumber);
    }

    private static void checkOnNumber(String maybeNumber) {
        try {
            Long.parseLong(maybeNumber);
        } catch (NumberFormatException e) {
            throw new NotANumberException("Переданная строка не является числом. " + e.getMessage());
        }
    }

    private static void checkOnNull(String maybeNumber){
        if(maybeNumber == null){
            throw new NotANumberException("Переданная строка является null");
        }
    }
}
