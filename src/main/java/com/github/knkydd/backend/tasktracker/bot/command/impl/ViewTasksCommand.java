package com.github.knkydd.backend.tasktracker.bot.command.impl;

import com.github.knkydd.backend.tasktracker.bot.command.Command;
import com.github.knkydd.backend.tasktracker.bot.exception.NoSuchTaskInRepositoryException;
import com.github.knkydd.backend.tasktracker.bot.model.Task;
import com.github.knkydd.backend.tasktracker.bot.property.MessageProperty;
import com.github.knkydd.backend.tasktracker.bot.service.TaskService;
import com.github.knkydd.backend.tasktracker.bot.session.state.StateType;
import com.github.knkydd.backend.tasktracker.bot.telegram.BotContext;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service("/view")
@Slf4j
@AllArgsConstructor
public class ViewTasksCommand implements Command {

    private final MessageProperty property;

    private final TaskService taskService;

    @Override
    public void handle(BotContext botContext) {
        long chatId = botContext.chatId();
        List<Task> tasks = getTasksByChatId(chatId);
        String text = description() + "\n" + createTextWithTasks(tasks);
        botContext.reply(text);
    }

    private List<Task> getTasksByChatId(long chatId) {
        try{
            return taskService.findAllByChatId(chatId);
        } catch (NoSuchTaskInRepositoryException e){
            log.error(e.getMessage());
            return Collections.<Task>emptyList();
        }
    }

    private String createTextWithTasks(List<Task> tasks) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Task task : tasks) {
            stringBuilder.append("Id задачи: ").append(task.getTaskId()).append("\n");
            stringBuilder.append("Категория задачи: ").append(task.getCategory().getName()).append("\n");
            stringBuilder.append("Описание задачи: ").append(task.getDescription()).append("\n");
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

    @Override
    public String command() {
        return "/view";
    }

    @Override
    public String description() {
        return property.getView();
    }

    @Override
    public StateType nextState() {
        return StateType.IDLE;
    }
}
