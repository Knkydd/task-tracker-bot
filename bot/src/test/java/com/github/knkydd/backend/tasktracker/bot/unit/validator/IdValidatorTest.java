package com.github.knkydd.backend.tasktracker.bot.unit.validator;


import com.github.knkydd.backend.tasktracker.bot.exception.NotANumberException;
import com.github.knkydd.backend.tasktracker.bot.validator.IdValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class IdValidatorTest {

    @Test
    @DisplayName("Тест с правильными входными данными")
    void testValid(){
        Assertions.assertDoesNotThrow(() -> IdValidator.checkValidated("4"));
    }

    @Test
    @DisplayName("Проверка на текст")
    void testWithText(){
        Assertions.assertThrows(NotANumberException.class, () -> IdValidator.checkValidated("ываыва"));
    }

    @Test
    @DisplayName("Проверка на текст вместе с цифрами")
    void testWithTextAndNumbers(){
        Assertions.assertThrows(NotANumberException.class, () -> IdValidator.checkValidated("dsdfsd3432dsfs"));
    }

    @Test
    @DisplayName("Проверка на null")
    void testNull(){
        Assertions.assertThrows(NotANumberException.class, () -> IdValidator.checkValidated(null));
    }

}
