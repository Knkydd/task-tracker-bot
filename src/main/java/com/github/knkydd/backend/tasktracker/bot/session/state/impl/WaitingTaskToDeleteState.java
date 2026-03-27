package com.github.knkydd.backend.tasktracker.bot.session.state.impl;

import com.github.knkydd.backend.tasktracker.bot.property.MessageProperty;
import com.github.knkydd.backend.tasktracker.bot.service.TaskService;
import com.github.knkydd.backend.tasktracker.bot.session.IdValidator;
import com.github.knkydd.backend.tasktracker.bot.session.SessionService;
import com.github.knkydd.backend.tasktracker.bot.session.UserSession;
import com.github.knkydd.backend.tasktracker.bot.session.state.StateType;
import com.github.knkydd.backend.tasktracker.bot.session.state.UserState;
import com.github.knkydd.backend.tasktracker.bot.telegram.BotContext;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class WaitingTaskToDeleteState implements UserState {

    private final MessageProperty property;

    private final SessionService service;

    private final TaskService taskService;

    private final IdValidator validator;

    @Override
    public StateType getStateType() {
        return StateType.WAITING_TASK_TO_DELETE;
    }

    @Override
    public boolean handle(BotContext botContext, UserSession session) {
        String maybeNumber = botContext.message();
        long chatId = botContext.chatId();
        if(!validator.isValidated(maybeNumber)){
            log.warn("Получено не число. Ошибка выполнения команды");
            service.reset(chatId);
            return false;
        }
        long taskId = Long.parseLong(maybeNumber);

        if(!taskService.deleteByTaskIdAndChatId(taskId,chatId)){
            log.warn("Нельзя удалить данную таску");
            service.reset(chatId);
            return false;
        }

        log.info("Удаление прошло успешно");

        sendCompleteText(botContext);
        service.reset(chatId);
        return true;
    }

    private void sendCompleteText(BotContext botContext){
        String text = property.getDeleteTask().getComplete();
        botContext.reply(text);
    }
}
