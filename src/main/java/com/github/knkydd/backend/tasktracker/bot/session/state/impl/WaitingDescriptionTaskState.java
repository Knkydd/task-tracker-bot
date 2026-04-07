package com.github.knkydd.backend.tasktracker.bot.session.state.impl;

import com.github.knkydd.backend.tasktracker.bot.exception.GetOrCreateCategoryException;
import com.github.knkydd.backend.tasktracker.bot.exception.GetOrCreateUserException;
import com.github.knkydd.backend.tasktracker.bot.exception.SaveTaskException;
import com.github.knkydd.backend.tasktracker.bot.model.Task;
import com.github.knkydd.backend.tasktracker.bot.model.TaskCategory;
import com.github.knkydd.backend.tasktracker.bot.model.User;
import com.github.knkydd.backend.tasktracker.bot.property.MessageProperty;
import com.github.knkydd.backend.tasktracker.bot.service.TaskCategoryService;
import com.github.knkydd.backend.tasktracker.bot.service.TaskService;
import com.github.knkydd.backend.tasktracker.bot.service.UserService;
import com.github.knkydd.backend.tasktracker.bot.session.UserSession;
import com.github.knkydd.backend.tasktracker.bot.session.state.StateType;
import com.github.knkydd.backend.tasktracker.bot.session.state.UserState;
import com.github.knkydd.backend.tasktracker.bot.telegram.BotContext;
import com.github.knkydd.backend.tasktracker.bot.validator.DescriptionValidator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class WaitingDescriptionTaskState implements UserState {

    private final MessageProperty property;

    private final TaskService taskService;

    private final UserService userService;

    private final TaskCategoryService taskCategoryService;


    @Override
    public StateType getStateType() {
        return StateType.WAITING_DESCRIPTION_TASK;
    }

    @Override
    public StateType getNextStateType() {
        return StateType.IDLE;
    }

    @Override
    public boolean handle(BotContext botContext, UserSession session) {
        return checkValidation(botContext) && saveTask(botContext, session);
    }

    private boolean checkValidation(BotContext botContext) {
        try {
            String description = botContext.message();
            DescriptionValidator.checkValidated(description);
            return true;
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            sendTextErrorDescriptionValidate(botContext);
        } catch (Exception e) {
            log.error("Возникла неизвестная ошибка во время валидации. {}", e.getMessage());
        }
        return false;
    }

    private boolean saveTask(BotContext botContext, UserSession session) {
        try {
            long chatId = botContext.chatId();
            String description = botContext.message();

            User user = userService.getOrCreateUser(chatId);
            String category = session.getCategory();
            TaskCategory taskCategory = taskCategoryService.getOrCreateCategory(category);
            Task task = new Task(taskCategory, user, description);

            taskService.saveTask(task);

            sendTextCompleteAdd(botContext);
            return true;
        } catch (GetOrCreateUserException e) {
            log.error(e.getMessage());
            sendTextErrorDescriptionSaveUser(botContext);
        } catch (GetOrCreateCategoryException e) {
            log.error(e.getMessage());
            //TODO: Добавить сообщение ошибки создания категории
        } catch (SaveTaskException e) {
            log.error(e.getMessage());
            sendTextErrorDescriptionSaveTask(botContext);
        } catch (Exception e) {
            log.error("Возникла неизвестная ошибка во время сохранения задачи. {}", e.getMessage());
        }
        return false;
    }

    private void sendTextErrorDescriptionValidate(BotContext botContext) {
        String text = property.getErrors().getAddErrors().getDescriptionValidate();
        botContext.reply(text);
    }

    private void sendTextErrorDescriptionSaveUser(BotContext botContext) {
        String text = property.getErrors().getAddErrors().getDescriptionSaveUser();
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
}
