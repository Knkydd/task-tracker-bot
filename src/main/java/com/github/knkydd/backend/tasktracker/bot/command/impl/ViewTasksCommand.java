package com.github.knkydd.backend.tasktracker.bot.command.impl;

import com.github.knkydd.backend.tasktracker.bot.command.Command;
import com.github.knkydd.backend.tasktracker.bot.model.Task;
import com.github.knkydd.backend.tasktracker.bot.service.TaskService;
import com.github.knkydd.backend.tasktracker.bot.telegram.BotContext;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("/view")
@Slf4j
@AllArgsConstructor
public class ViewTasksCommand implements Command {

    private final TaskService taskService;

    @Override
    public void handle(BotContext botContext) {
        long chatId = botContext.chatId();
        List<Task> tasks = getTasksByChatId(chatId);
        String text = createTextWithTasks(tasks);
        botContext.reply(text);
    }

    @Override
    public String command() {
        return "/view";
    }

    @Override
    public String description() {
        return "";
    }

    private List<Task> getTasksByChatId(long chatId) {
        return taskService.findAllByChatId(chatId);
    }

    private String createTextWithTasks(List<Task> tasks) {
        StringBuilder stringBuilder = new StringBuilder("Ваши задачи:\n");
        for (Task task : tasks) {
            stringBuilder.append("Id задачи: ").append(task.getTaskId()).append("\n");
            stringBuilder.append("Категория задачи: ").append(task.getCategory().getName()).append("\n");
            stringBuilder.append("Описание задачи: ").append(task.getDescription()).append("\n");
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }
}
