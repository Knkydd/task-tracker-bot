package com.github.knkydd.backend.tasktracker.bot.command;

import com.github.knkydd.backend.tasktracker.bot.telegram.BotContext;

public interface Command {

    void handle(BotContext botContext);

    String command();

    String description();
}
