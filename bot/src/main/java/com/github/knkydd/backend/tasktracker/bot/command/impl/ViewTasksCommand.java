package com.github.knkydd.backend.tasktracker.bot.command.impl;

import com.github.knkydd.backend.tasktracker.bot.command.Command;
import com.github.knkydd.backend.tasktracker.bot.exception.TaskGetException;
import com.github.knkydd.backend.tasktracker.bot.property.MessageProperty;
import com.github.knkydd.backend.tasktracker.bot.session.state.StateType;
import com.github.knkydd.backend.tasktracker.bot.telegram.BotContext;
import com.github.knkydd.backend.tasktracker.bot.utility.text.TextGenerator;
import com.github.knkydd.backend.tasktracker.bot.web.http.TaskGateway;
import com.github.knkydd.backend.tasktracker.bot.web.responses.TaskResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("/view")
@Slf4j
@RequiredArgsConstructor
public class ViewTasksCommand implements Command {

    private final MessageProperty property;

    private final TaskGateway gateway;

    @Override
    public void handle(BotContext botContext) {
        try {
            List<TaskResponseDto> tasks = gateway.getTasks(botContext.chatId());
            String text = getTextWithTasksToReply(tasks);
            botContext.reply(text);
        } catch (TaskGetException e) {
            log.error(e.getMessage());
            sendTextErrorGettingTasks(botContext);
        } catch (Exception e){
            log.error("Возникла неизвестная ошибка. {}", e.getMessage());
            sendTextUnknownError(botContext);
        }
    }

    private String getTextWithTasksToReply(List<TaskResponseDto> tasks) {
        return description() + "\n" + createTextWithTasks(tasks);
    }

    private String createTextWithTasks(List<TaskResponseDto> tasks) {
        String template = property.getViewTemplate();
        return TextGenerator.generateForViewCommand(template, tasks);
    }

    private void sendTextErrorGettingTasks(BotContext botContext) {
        String text = property.getErrors().getViewErrors().getGettingTasks();
        botContext.reply(text);
    }

    private void sendTextUnknownError(BotContext botContext){
        String text = property.getErrors().getUnknownError();
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
