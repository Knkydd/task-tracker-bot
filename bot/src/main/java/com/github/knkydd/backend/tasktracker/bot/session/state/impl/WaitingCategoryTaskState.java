package com.github.knkydd.backend.tasktracker.bot.session.state.impl;

import com.github.knkydd.backend.tasktracker.bot.property.MessageProperty;
import com.github.knkydd.backend.tasktracker.bot.session.UserSession;
import com.github.knkydd.backend.tasktracker.bot.session.state.StateType;
import com.github.knkydd.backend.tasktracker.bot.session.state.UserState;
import com.github.knkydd.backend.tasktracker.bot.telegram.BotContext;
import com.github.knkydd.backend.tasktracker.bot.validator.CategoryValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@Slf4j
@RequiredArgsConstructor
public class WaitingCategoryTaskState implements UserState {

    private final MessageProperty property;

    @Override
    public StateType getStateType() {
        return StateType.WAITING_CATEGORY_TASK;
    }

    @Override
    public StateType getNextStateType() {
        return StateType.WAITING_DESCRIPTION_TASK;
    }

    @Override
    public boolean handle(BotContext botContext, UserSession session) {
        try {
            String category = botContext.message();
            CategoryValidator.checkValidated(category);

            sendTextAddDescriptionText(botContext);
            session.setCategory(category);

            return true;

        } catch (IllegalArgumentException e) {
            log.warn("Возникла ошибка валидации. {}", e.getMessage());
            sendTextErrorCategoryValidate(botContext);
        } catch (Exception e) {
            log.error("Возникла неизвестная ошибка! {}", e.getMessage());
        }
        return false;
    }

    private void sendTextErrorCategoryValidate(BotContext botContext) {
        String text = property.getErrors().getAddErrors().getCategoryValidate();
        botContext.reply(text);
    }

    private void sendTextAddDescriptionText(BotContext botContext) {
        String text = property.getAddTask().getAddDescription();
        botContext.reply(text);
    }
}
