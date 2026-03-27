package com.github.knkydd.backend.tasktracker.bot.session.state.impl;

import com.github.knkydd.backend.tasktracker.bot.model.Task;
import com.github.knkydd.backend.tasktracker.bot.model.TaskCategory;
import com.github.knkydd.backend.tasktracker.bot.model.User;
import com.github.knkydd.backend.tasktracker.bot.property.MessageProperty;
import com.github.knkydd.backend.tasktracker.bot.repository.TaskRepository;
import com.github.knkydd.backend.tasktracker.bot.repository.UserRepository;
import com.github.knkydd.backend.tasktracker.bot.service.TaskService;
import com.github.knkydd.backend.tasktracker.bot.service.UserService;
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
public class WaitingDescriptionTaskState implements UserState {

    private final MessageProperty property;

    private final TaskService taskService;

    private final UserService userService;

    private final SessionService service;

    @Override
    public StateType getStateType() {
        return StateType.WAITING_DESCRIPTION_TASK;
    }

    @Override
    public boolean handle(BotContext botContext, UserSession session) {
        long chatId = botContext.chatId();
        String description = botContext.message();
        if (description.startsWith("/")) {
            log.warn("Пользователь {} прервал ввод категории таски другой командой. Команда: {}", chatId, description);
            sendWarningText(botContext);
            service.reset(chatId);
            return false;
        }

        Task task = new Task();
        TaskCategory category = session.getCategory();
        task.setDescription(description);
        task.setCategory(category);

        User user = userService.getOrCreateByChatId(chatId);
        task.setUser(user);

        log.info("Попытка сохранения таски с номером {}", task.getTaskId());

        taskService.saveAndFlush(task);

        log.info("Таска с номером {} успешно сохранена!", task.getTaskId());

        sendSuccessfullyText(botContext);
        service.reset(botContext.chatId());
        return true;
    }

    private void sendWarningText(BotContext botContext) {
        String text = property.getAddTask().getError();
        botContext.reply(text);
    }

    private void sendSuccessfullyText(BotContext botContext) {
        String text = property.getAddTask().getCompleteAdd();
        botContext.reply(text);
    }
}
