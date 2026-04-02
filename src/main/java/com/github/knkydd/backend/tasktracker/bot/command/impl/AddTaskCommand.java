package com.github.knkydd.backend.tasktracker.bot.command.impl;

import com.github.knkydd.backend.tasktracker.bot.command.Command;
import com.github.knkydd.backend.tasktracker.bot.property.MessageProperty;
import com.github.knkydd.backend.tasktracker.bot.session.SessionService;
import com.github.knkydd.backend.tasktracker.bot.session.UserSession;
import com.github.knkydd.backend.tasktracker.bot.session.state.StateType;
import com.github.knkydd.backend.tasktracker.bot.telegram.BotContext;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service("/add")
@Slf4j
@AllArgsConstructor
public class AddTaskCommand implements Command {

    private final MessageProperty property;

    private final SessionService service;

    @Override
    public void handle(BotContext botContext) {
        botContext.reply(description());
        log.info("Ожидание категории задачи");
        setStateToWaitingCategory(botContext.chatId());
    }

    @Override
    public String command() {
        return "/add";
    }

    @Override
    public String description() {
        return property.getAddTask().getAddCategory();
    }

    private void setStateToWaitingCategory(long chatId) {
        UserSession session = service.getOrCreate(chatId);
        session.setStateType(StateType.WAITING_CATEGORY_TASK);
        service.save(session);
    }
}
