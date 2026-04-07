package com.github.knkydd.backend.tasktracker.bot.command.impl;

import com.github.knkydd.backend.tasktracker.bot.command.Command;
import com.github.knkydd.backend.tasktracker.bot.exception.GetTaskListException;
import com.github.knkydd.backend.tasktracker.bot.model.Task;
import com.github.knkydd.backend.tasktracker.bot.property.MessageProperty;
import com.github.knkydd.backend.tasktracker.bot.service.TaskService;
import com.github.knkydd.backend.tasktracker.bot.session.state.StateType;
import com.github.knkydd.backend.tasktracker.bot.telegram.BotContext;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("/view")
@Slf4j
@AllArgsConstructor
public class ViewTasksCommand implements Command {

    private final MessageProperty property;

    private final TaskService taskService;

    @Override
    public void handle(BotContext botContext) {
        try {
            long chatId = botContext.chatId();
            String text = getTextWithTasksToReply(chatId);
            botContext.reply(text);
        } catch (GetTaskListException e) {
            log.error(e.getMessage());
            sendTextErrorGettingTasks(botContext);
        }
    }

    private String getTextWithTasksToReply(long chatId) {
        List<Task> tasks = getTasks(chatId);
        return description() + "\n" + createTextWithTasks(tasks);
    }

    private List<Task> getTasks(long chatId) {
        return taskService.getUserTasks(chatId);
    }

    private String createTextWithTasks(List<Task> tasks) {
        StringBuilder stringBuilder = new StringBuilder();
        String template = property.getViewTemplate();
        for (Task task : tasks) {
            stringBuilder.append(
                    String.format(template, task.getTaskId(), task.getCategory().getName(), task.getDescription())
            );
        }
        return stringBuilder.toString();
    }

    private void sendTextErrorGettingTasks(BotContext botContext) {
        String text = property.getErrors().getViewErrors().getGettingTasks();
        botContext.reply(text);
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
