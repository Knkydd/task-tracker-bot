package com.github.knkydd.backend.tasktracker.bot.command;

import com.github.knkydd.backend.tasktracker.bot.command.impl.UnknownCommand;
import com.github.knkydd.backend.tasktracker.bot.session.SessionService;
import com.github.knkydd.backend.tasktracker.bot.session.UserSession;
import com.github.knkydd.backend.tasktracker.bot.telegram.BotContext;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
@AllArgsConstructor
public class CommandHandler {

    private final Map<String, Command> commands;

    private final UnknownCommand unknownCommand;

    private final SessionService service;

    public void execute(BotContext botContext, UserSession session) {
        String message = botContext.message().split(" ")[0];
        Command command = commands.getOrDefault(message, unknownCommand);

        log.info("Выполнение команды {} ...", command.command());

        session.setStateType(command.nextState());

        command.handle(botContext);
    }

}
