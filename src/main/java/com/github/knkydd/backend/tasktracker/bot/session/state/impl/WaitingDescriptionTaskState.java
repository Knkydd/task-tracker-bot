package com.github.knkydd.backend.tasktracker.bot.session.state.impl;

import com.github.knkydd.backend.tasktracker.bot.exception.GetOrCreateUserException;
import com.github.knkydd.backend.tasktracker.bot.exception.SaveTaskException;
import com.github.knkydd.backend.tasktracker.bot.model.Task;
import com.github.knkydd.backend.tasktracker.bot.model.TaskCategory;
import com.github.knkydd.backend.tasktracker.bot.model.User;
import com.github.knkydd.backend.tasktracker.bot.property.MessageProperty;
import com.github.knkydd.backend.tasktracker.bot.service.TaskCategoryService;
import com.github.knkydd.backend.tasktracker.bot.service.TaskService;
import com.github.knkydd.backend.tasktracker.bot.service.UserService;
import com.github.knkydd.backend.tasktracker.bot.session.SessionService;
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

    private final SessionService service;


    @Override
    public StateType getStateType() {
        return StateType.WAITING_DESCRIPTION_TASK;
    }

    @Override
    public boolean handle(BotContext botContext, UserSession session) {
        try {
            long chatId = botContext.chatId();
            String description = botContext.message();

            DescriptionValidator.checkValidated(description);
            User user = userService.getOrCreateByChatId(chatId);
            TaskCategory category = taskCategoryService.getOrCreateCategory(session.getCategory());
            Task task = new Task(category, user, description);

            taskService.saveAndFlush(task);
            log.info("Таска с номером {} успешно сохранена!", task.getTaskId());

            sendTextCompleteAdd(botContext);
            service.reset(botContext.chatId());

            return true;
        } catch (IllegalArgumentException e) {
            log.warn(e.getMessage());
            sendTextErrorDescriptionValidate(botContext);
        } catch (GetOrCreateUserException e) {
            log.error(e.getMessage());
            sendTextErrorDescriptionSaveUser(botContext);
        } catch (SaveTaskException e) {
            log.error(e.getMessage());
            sendTextErrorDescriptionSaveTask(botContext);
        }
        return false;
    }

    private void sendTextErrorDescriptionValidate(BotContext botContext) {
        String text = property.getError().getAddErrors().getDescriptionValidate();
        botContext.reply(text);
    }

    private void sendTextErrorDescriptionSaveUser(BotContext botContext) {
        String text = property.getError().getAddErrors().getDescriptionSaveUser();
        botContext.reply(text);
    }

    private void sendTextErrorDescriptionSaveTask(BotContext botContext) {
        String text = property.getError().getAddErrors().getDescriptionSaveTask();
        botContext.reply(text);
    }

    private void sendTextCompleteAdd(BotContext botContext) {
        String text = property.getAddTask().getCompleteAdd();
        botContext.reply(text);
    }
}
