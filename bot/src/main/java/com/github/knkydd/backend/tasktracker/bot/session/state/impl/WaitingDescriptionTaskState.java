package com.github.knkydd.backend.tasktracker.bot.session.state.impl;

import com.github.knkydd.backend.tasktracker.bot.exception.TaskSaveException;
import com.github.knkydd.backend.tasktracker.bot.property.MessageProperty;
import com.github.knkydd.backend.tasktracker.bot.session.UserSession;
import com.github.knkydd.backend.tasktracker.bot.session.state.StateType;
import com.github.knkydd.backend.tasktracker.bot.session.state.UserState;
import com.github.knkydd.backend.tasktracker.bot.telegram.BotContext;
import com.github.knkydd.backend.tasktracker.bot.utility.validator.DescriptionValidator;
import com.github.knkydd.backend.tasktracker.bot.web.http.TaskGateway;
import com.github.knkydd.backend.tasktracker.bot.web.requests.CreateTaskRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class WaitingDescriptionTaskState implements UserState {

    private final MessageProperty property;

    private final TaskGateway gateway;

    @Override
    public StateType getStateType() {
        return StateType.WAITING_DESCRIPTION_TASK;
    }

    @Override
    public StateType getNextStateType() {
        return StateType.IDLE;
    }

    @Override
    public void handle(BotContext botContext, UserSession session) {
        try {
            checkValidation(botContext);
            saveTask(botContext, session);
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            sendTextErrorDescriptionValidate(botContext);
        } catch (TaskSaveException e) {
            log.error(e.getMessage());
            sendTextErrorDescriptionSaveTask(botContext);
        } catch (Exception e) {
            log.error("Возникла неизвестная ошибка во время сохранения задачи. {}", e.getMessage());
            sendTextUnknownError(botContext);
        }
    }

    private void checkValidation(BotContext botContext) {
        String description = botContext.message();
        DescriptionValidator.checkValidated(description);
    }

    private void saveTask(BotContext botContext, UserSession session) {
        long chatId = botContext.chatId();
        String category = session.getCategory();
        String description = botContext.message();
        var taskDto = new CreateTaskRequestDto(category, description);
        gateway.saveTask(chatId, taskDto);
        log.info("Задача успешно сохранена");
        sendTextCompleteAdd(botContext);
    }

    private void sendTextErrorDescriptionValidate(BotContext botContext) {
        String text = property.getErrors().getAddErrors().getDescriptionValidate();
        botContext.reply(text);
    }

    private void sendTextErrorDescriptionSaveTask(BotContext botContext) {
        String text = property.getErrors().getAddErrors().getDescriptionSaveTask();
        botContext.reply(text);
    }

    private void sendTextCompleteAdd(BotContext botContext) {
        String text = property.getAddTask().getCompleteAdd();
        botContext.reply(text);
    }

    private void sendTextUnknownError(BotContext botContext) {
        String text = property.getErrors().getUnknownError();
        botContext.reply(text);
    }
}
