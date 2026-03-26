package com.github.knkydd.backend.tasktracker.bot.config;

import com.github.knkydd.backend.tasktracker.bot.property.TelegramProperty;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.impl.SleepUpdatesHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TelegramBotConfig {

    @Bean
    public TelegramBot telegramBot(TelegramProperty property){
        var builder = new TelegramBot.Builder(property.getToken())
                .apiUrl(property.getUri())
                .updateListener(new SleepUpdatesHandler(property
                        .getSleepUpdateListener()
                        .toMillis()
                ));

        return builder.build();
    }
}
