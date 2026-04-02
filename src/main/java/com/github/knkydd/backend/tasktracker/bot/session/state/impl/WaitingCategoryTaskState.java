package com.github.knkydd.backend.tasktracker.bot.session.state.impl;

import com.github.knkydd.backend.tasktracker.bot.exception.GetOrCreateCategoryException;
import com.github.knkydd.backend.tasktracker.bot.model.TaskCategory;
import com.github.knkydd.backend.tasktracker.bot.property.MessageProperty;
import com.github.knkydd.backend.tasktracker.bot.service.TaskCategoryService;
import com.github.knkydd.backend.tasktracker.bot.session.SessionService;
import com.github.knkydd.backend.tasktracker.bot.session.UserSession;
import com.github.knkydd.backend.tasktracker.bot.session.state.StateType;
import com.github.knkydd.backend.tasktracker.bot.session.state.UserState;
import com.github.knkydd.backend.tasktracker.bot.telegram.BotContext;
import com.github.knkydd.backend.tasktracker.bot.validator.CategoryValidator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class WaitingCategoryTaskState implements UserState {

    private final MessageProperty property;

    private final CategoryValidator validator;

    private final TaskCategoryService taskCategoryService;

    private final SessionService service;

    @Override
    public StateType getStateType() {
        return StateType.WAITING_CATEGORY_TASK;
    }

    @Override
    public boolean handle(BotContext botContext, UserSession session) {
        long chatId = botContext.chatId();
        try {
            String category = botContext.message();
            validate(category);
            Optional<TaskCategory> taskCategory = taskCategoryService.getOrCreateCategory(category);

            sendTextAddDescriptionText(botContext);
            session.setCategory(taskCategory.get());
            session.setStateType(StateType.WAITING_DESCRIPTION_TASK);

            return true;
        } catch (IllegalArgumentException e) {
            log.warn("Возникла ошибка валидации. {}", e.getMessage());
            sendTextErrorCategoryValidate(botContext);
            service.reset(chatId);
            return false;
        } catch (GetOrCreateCategoryException e) {
            log.error(e.getMessage());
            sendTextErrorCategorySave(botContext);
            service.reset(chatId);
            return false;
        } catch (Exception e) {
            log.error("Возникла неизвестная ошибка! {}", e.getMessage());
            service.reset(chatId);
            return false;
        }
    }

    private void validate(String category) throws IllegalArgumentException {
        validator.isValidated(category);
    }

    private void sendTextErrorCategoryValidate(BotContext botContext) {
        String text = property.getError().getAddErrors().getCategoryValidate();
        botContext.reply(text);
    }

    private void sendTextErrorCategorySave(BotContext botContext) {
        String text = property.getError().getAddErrors().getCategorySave();
        botContext.reply(text);

    }

    private void sendTextAddDescriptionText(BotContext botContext) {
        String text = property.getAddTask().getAddDescription();
        botContext.reply(text);
    }
}
