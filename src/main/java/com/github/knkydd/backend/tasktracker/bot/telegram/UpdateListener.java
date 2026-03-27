package com.github.knkydd.backend.tasktracker.bot.telegram;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
@Slf4j
public class UpdateListener implements UpdatesListener {

    private final TelegramBot telegramBot;

    private final BotClient botClient;

    private final UpdateProcessor updateProcessor;

    @Override
    public int process(List<Update> list) {
        for (Update update : list) {
            if (update.message() == null || update.message().text() == null)
                continue;
            log.info("Бот получил новое сообщение. Переадресация процессору...");
            updateProcessor.process(new BotContext(update, botClient));
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    @PostConstruct
    public void start() {
        telegramBot.setUpdatesListener(this);
    }

}
