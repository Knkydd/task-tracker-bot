package com.github.knkydd.backend.tasktracker.bot.unit.validator;

import com.github.knkydd.backend.tasktracker.bot.validator.CategoryValidator;
import com.github.knkydd.backend.tasktracker.bot.validator.DescriptionValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class DescriptionValidatorTest {

    @Test
    @DisplayName("Тест с правильными данными")
    void testTrue() {
        Assertions.assertDoesNotThrow(() -> DescriptionValidator.checkValidated("Ok!"));
    }

    @Test
    @DisplayName("Проверка на ввод команды")
    void testWithCommand() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> DescriptionValidator.checkValidated("/Про сто команда"));
    }

    @Test
    @DisplayName("Проверка на null")
    void testOnNull(){
        Assertions.assertThrows(IllegalArgumentException.class, () -> DescriptionValidator.checkValidated(null));
    }

    @Test
    @DisplayName("Проверка, что категория это число")
    void testOnCategoryIsNumber(){
        Assertions.assertThrows(IllegalArgumentException.class, () -> DescriptionValidator.checkValidated("34234212"));
    }

    @Test
    @DisplayName("Проверка, что в категории есть числа")
    void testOnNumberInCategory(){
        Assertions.assertThrows(IllegalArgumentException.class, () -> DescriptionValidator.checkValidated("прив3т"));
    }
}
