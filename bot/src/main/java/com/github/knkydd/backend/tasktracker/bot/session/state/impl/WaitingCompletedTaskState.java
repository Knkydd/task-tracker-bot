package com.github.knkydd.backend.tasktracker.bot.session.state.impl;

import com.github.knkydd.backend.tasktracker.bot.exception.NotANumberException;
import com.github.knkydd.backend.tasktracker.bot.exception.TaskDeleteException;
import com.github.knkydd.backend.tasktracker.bot.exception.TaskNotFoundException;
import com.github.knkydd.backend.tasktracker.bot.property.MessageProperty;
import com.github.knkydd.backend.tasktracker.bot.session.UserSession;
import com.github.knkydd.backend.tasktracker.bot.session.state.StateType;
import com.github.knkydd.backend.tasktracker.bot.session.state.UserState;
import com.github.knkydd.backend.tasktracker.bot.telegram.BotContext;
import com.github.knkydd.backend.tasktracker.bot.utility.validator.IdValidator;
import com.github.knkydd.backend.tasktracker.bot.web.http.TaskGateway;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class WaitingCompletedTaskState implements UserState {

    private final MessageProperty property;

    private final TaskGateway gateway;

    @Override
    public StateType getStateType() {
        return StateType.WAITING_COMPLETED_TASK;
    }

    @Override
    public StateType getNextStateType() {
        return StateType.IDLE;
    }

    @Override
    public void handle(BotContext botContext, UserSession session) {
        try {
            checkIdCorrect(botContext);
            deleteTask(botContext);
        } catch (NotANumberException e) {
            log.error(e.getMessage());
            sendTextErrorNotANumber(botContext);
        } catch (TaskDeleteException e) {
            log.error(e.getMessage());
            sendTextErrorWithDelete(botContext);
        } catch (TaskNotFoundException e) {
            log.error(e.getMessage());
            sendTextErrorTaskNotExists(botContext);
        } catch (Exception e) {
            log.error("Возникла неизвестная ошибка во время проверки валидации номера задачи. {}", e.getMessage());
            sendTextUnknownError(botContext);
        }
    }

    private void checkIdCorrect(BotContext botContext) {
        String maybeNumber = botContext.message();
        IdValidator.checkValidated(maybeNumber);
    }

    private void deleteTask(BotContext botContext) {
        long chatId = botContext.chatId();
        long taskId = Long.parseLong(botContext.message());
        gateway.deleteTask(chatId, taskId);
        sendTextCompleteSuccess(botContext);
    }

    private void sendTextCompleteSuccess(BotContext botContext) {
        String text = property.getCompleteTask().getCompleteSuccess();
        botContext.reply(text);
    }

    private void sendTextErrorNotANumber(BotContext botContext) {
        String text = property.getErrors().getIdValidateErrors().getNotANumber();
        botContext.reply(text);
    }

    private void sendTextErrorTaskNotExists(BotContext botContext) {
        String text = property.getErrors().getIdValidateErrors().getTaskNotExists();
        botContext.reply(text);
    }

    private void sendTextErrorWithDelete(BotContext botContext) {
        String text = property.getErrors().getCompleteErrors().getDbDelete();
        botContext.reply(text);
    }

    private void sendTextUnknownError(BotContext botContext) {
        String text = property.getErrors().getUnknownError();
        botContext.reply(text);
    }
}
