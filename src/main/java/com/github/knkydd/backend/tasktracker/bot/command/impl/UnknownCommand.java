package com.github.knkydd.backend.tasktracker.bot.command.impl;

import com.github.knkydd.backend.tasktracker.bot.command.Command;
import com.github.knkydd.backend.tasktracker.bot.property.MessageProperty;
import com.github.knkydd.backend.tasktracker.bot.telegram.BotContext;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class UnknownCommand implements Command {

    private final MessageProperty property;

    @Override
    public void handle(BotContext botContext) {
        botContext.reply(description());
    }

    @Override
    public String command() {
        return "";
    }

    @Override
    public String description() {
        return property.getUnknown();
    }
}
