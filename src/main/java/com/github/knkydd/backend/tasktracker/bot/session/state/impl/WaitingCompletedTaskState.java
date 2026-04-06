package com.github.knkydd.backend.tasktracker.bot.session.state.impl;

import com.github.knkydd.backend.tasktracker.bot.exception.DeleteTaskException;
import com.github.knkydd.backend.tasktracker.bot.exception.NoSuchTaskInRepositoryException;
import com.github.knkydd.backend.tasktracker.bot.exception.NotANumberException;
import com.github.knkydd.backend.tasktracker.bot.property.MessageProperty;
import com.github.knkydd.backend.tasktracker.bot.service.TaskService;
import com.github.knkydd.backend.tasktracker.bot.session.SessionService;
import com.github.knkydd.backend.tasktracker.bot.session.UserSession;
import com.github.knkydd.backend.tasktracker.bot.session.state.StateType;
import com.github.knkydd.backend.tasktracker.bot.session.state.UserState;
import com.github.knkydd.backend.tasktracker.bot.telegram.BotContext;
import com.github.knkydd.backend.tasktracker.bot.validator.IdValidator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class WaitingCompletedTaskState implements UserState {

    private final MessageProperty property;

    private final SessionService service;

    private final TaskService taskService;

    @Override
    public StateType getStateType() {
        return StateType.WAITING_COMPLETED_TASK;
    }

    @Override
    public boolean handle(BotContext botContext, UserSession session) {
        try {
            String maybeNumber = botContext.message();
            long chatId = botContext.chatId();
            long taskId = validateAndGetTaskId(maybeNumber, chatId);
            taskService.deleteByTaskIdAndChatId(taskId, chatId);
            sendTextCompleteSuccess(botContext);
            log.info("Таска успешно выполнена и удалена");
            service.reset(chatId);
            return true;
        } catch (NotANumberException | NoSuchTaskInRepositoryException e) {
            sendTextErrorIdValidate(botContext);
        } catch (DeleteTaskException e) {
            sendTextErrorWithDelete(botContext);
            log.error(e.getMessage());
        } catch (Exception e) {
            log.error("Возникла неизвестная ошибка! {}", e.getMessage());
        }
        return false;
    }

    private long validateAndGetTaskId(String maybeNumber, long chatId) {
        IdValidator.checkValidated(maybeNumber);
        long taskId = Long.parseLong(maybeNumber);
        taskService.checkTaskExists(taskId, chatId);
        return taskId;
    }

    private void sendTextCompleteSuccess(BotContext botContext) {
        String text = property.getCompleteTask().getCompleteSuccess();
        botContext.reply(text);
    }

    private void sendTextErrorIdValidate(BotContext botContext) {
        String text = property.getError().getCompleteErrors().getIdValidate();
        botContext.reply(text);
    }

    private void sendTextErrorWithDelete(BotContext botContext) {
        String text = property.getError().getCompleteErrors().getDbDelete();
        botContext.reply(text);
    }
}
