package com.github.knkydd.backend.tasktracker.bot.telegram;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class BotClient {

    private final TelegramBot telegramBot;

    public void sendMessage(String message, long chatId) {
        try {
            log.info("Попытка отправки сообщения пользователю: {}. Сообщение: {}", chatId, message);

            SendResponse response = telegramBot.execute(new SendMessage(chatId, message));
            if (response.isOk()) {
                log.info("Сообщение успешно отправлено пользователю {}", chatId);
            } else {
                log.warn("Возникла ошибка во время отправки сообщения пользователю {}", chatId);
            }
        } catch (Exception e) {
            log.error("Возник сбой клиента или разрыв соединения. Отправка сообщения пользователю {} прервана", chatId);

        }
    }
}
