package com.github.knkydd.backend.tasktracker.bot.telegram;

import com.pengrad.telegrambot.model.Update;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BotContext {

    private final Update update;

    private final BotClient bot;

    public long chatId(){
        return update.message().chat().id();
    }

    public String message(){
        return update.message().text();
    }

    public void reply(String text){
        bot.sendMessage(text, chatId());
    }
}
