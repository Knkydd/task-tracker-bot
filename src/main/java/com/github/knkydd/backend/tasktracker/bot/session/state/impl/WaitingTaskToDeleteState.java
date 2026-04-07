package com.github.knkydd.backend.tasktracker.bot.session.state.impl;

import com.github.knkydd.backend.tasktracker.bot.exception.DeleteTaskException;
import com.github.knkydd.backend.tasktracker.bot.exception.NoSuchTaskInRepositoryException;
import com.github.knkydd.backend.tasktracker.bot.exception.NotANumberException;
import com.github.knkydd.backend.tasktracker.bot.property.MessageProperty;
import com.github.knkydd.backend.tasktracker.bot.service.TaskService;
import com.github.knkydd.backend.tasktracker.bot.validator.IdValidator;
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

    private final TaskService taskService;

    @Override
    public StateType getStateType() {
        return StateType.WAITING_TASK_TO_DELETE;
    }

    @Override
    public StateType getNextStateType() {
        return StateType.IDLE;
    }

    @Override
    public boolean handle(BotContext botContext, UserSession session) {
        return checkIdCorrect(botContext) && deleteTask(botContext);
    }

    private boolean checkIdCorrect(BotContext botContext) {
        try {
            String maybeNumber = botContext.message();
            IdValidator.checkValidated(maybeNumber);
            long taskId = Long.parseLong(maybeNumber);
            long chatId = botContext.chatId();
            taskService.checkTaskExists(taskId, chatId);
            return true;
        } catch (NotANumberException e) {
            log.error(e.getMessage());
            sendTextErrorNotANumber(botContext);
        } catch (NoSuchTaskInRepositoryException e) {
            log.error(e.getMessage());
            sendTextErrorTaskNotExists(botContext);
        } catch (Exception e) {
            log.error("Возникла неизвестная ошибка во время проверки валидации номера задачи. {}", e.getMessage());
        }
        return false;
    }

    private boolean deleteTask(BotContext botContext) {
        try {
            long chatId = botContext.chatId();
            long taskId = Long.parseLong(botContext.message());
            taskService.deleteTask(taskId, chatId);
            sendTextCompleteDeleted(botContext);
            return true;
        } catch (DeleteTaskException e) {
            log.error(e.getMessage());
            sendTextErrorDbDelete(botContext);
        }
        return false;
    }

    private void sendTextCompleteDeleted(BotContext botContext) {
        String text = property.getDeleteTask().getCompleteDelete();
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

    private void sendTextErrorDbDelete(BotContext botContext) {
        String text = property.getErrors().getDeleteErrors().getDbDelete();
        botContext.reply(text);
    }
}
