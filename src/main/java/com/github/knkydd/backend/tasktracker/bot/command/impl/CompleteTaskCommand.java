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

@Service("/complete")
@Slf4j
@AllArgsConstructor
public class CompleteTaskCommand implements Command {

    private final MessageProperty property;

    private final SessionService service;

    @Override
    public void handle(BotContext botContext) {
        botContext.reply(description());
        log.info("Ожидание номера выполненной задачи");
        setStateToWaitingCompleteTask(botContext.chatId());
    }

    @Override
    public String command() {
        return "/complete";
    }

    @Override
    public String description() {
        return property.getCompleteTask().getProcess();
    }

    private void setStateToWaitingCompleteTask(long chatId){
        UserSession session = service.getOrCreate(chatId);
        session.setStateType(StateType.WAITING_COMPLETED_TASK);
        service.save(session);
    }
}
