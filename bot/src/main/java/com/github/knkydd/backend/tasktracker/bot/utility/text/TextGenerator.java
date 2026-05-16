package com.github.knkydd.backend.tasktracker.bot.utility.text;

import com.github.knkydd.backend.tasktracker.bot.web.responses.TaskResponseDto;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class TextGenerator {

    public static String generateForViewCommand(String template, List<TaskResponseDto> tasks) {
        StringBuilder stringBuilder = new StringBuilder();
        for (TaskResponseDto task : tasks) {
            stringBuilder.append(
                    String.format(template, task.taskId(), task.category(), task.description())
            );
        }
        return stringBuilder.toString();
    }
}
