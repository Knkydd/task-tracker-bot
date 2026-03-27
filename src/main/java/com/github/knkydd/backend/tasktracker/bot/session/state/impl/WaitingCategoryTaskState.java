package com.github.knkydd.backend.tasktracker.bot.session.state.impl;

import com.github.knkydd.backend.tasktracker.bot.model.TaskCategory;
import com.github.knkydd.backend.tasktracker.bot.property.MessageProperty;
import com.github.knkydd.backend.tasktracker.bot.repository.TaskCategoryRepository;
import com.github.knkydd.backend.tasktracker.bot.service.TaskCategoryService;
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
public class WaitingCategoryTaskState implements UserState {

    private final MessageProperty property;

    private final TaskCategoryService taskCategoryService;

    @Override
    public StateType getStateType() {
        return StateType.WAITING_CATEGORY_TASK;
    }

    //TODO: Добавить обработку ошибку
    @Override
    public boolean handle(BotContext botContext, UserSession session) {

        long chatId = botContext.chatId();
        String category = botContext.message();
        if (category.startsWith("/")) {
            log.warn("Пользователь {} прервал ввод категории таски другой командой. Команда: {}", chatId, category);
            sendWarningText(botContext);
            return false;
        }
        sendText(botContext);

        //TODO: ЛОГ

        TaskCategory taskCategory = taskCategoryService.getOrCreateCategory(category);
        session.setCategory(taskCategory);

        //TODO: ЛОГ
        session.setStateType(StateType.WAITING_DESCRIPTION_TASK);

        return true;
    }

    private void sendWarningText(BotContext botContext) {
        String text = property.getAddTask().getError();
        botContext.reply(text);
    }

    private void sendText(BotContext botContext) {
        String text = property.getAddTask().getAddDescription();
        botContext.reply(text);
    }
}
