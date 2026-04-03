package com.github.knkydd.backend.tasktracker.bot.command.impl;

import com.github.knkydd.backend.tasktracker.bot.command.Command;
import com.github.knkydd.backend.tasktracker.bot.property.MessageProperty;
import com.github.knkydd.backend.tasktracker.bot.session.SessionService;
import com.github.knkydd.backend.tasktracker.bot.session.state.StateType;
import com.github.knkydd.backend.tasktracker.bot.telegram.BotContext;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service("/delete")
@Slf4j
@AllArgsConstructor
public class DeleteTaskCommand implements Command {

    private final MessageProperty property;

    @Override
    public void handle(BotContext botContext) {
        botContext.reply(description());
        log.info("Ожидание номера задачи, которую нужно удалить");
    }

    @Override
    public String command() {
        return "/delete";
    }

    @Override
    public String description() {
        return property.getDeleteTask().getProcessDelete();
    }

    @Override
    public StateType nextState() {
        return StateType.WAITING_TASK_TO_DELETE;
    }
}
