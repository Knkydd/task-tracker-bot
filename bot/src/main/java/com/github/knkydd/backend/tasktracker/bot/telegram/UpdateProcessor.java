package com.github.knkydd.backend.tasktracker.bot.telegram;

import com.github.knkydd.backend.tasktracker.bot.command.CommandHandler;
import com.github.knkydd.backend.tasktracker.bot.session.SessionService;
import com.github.knkydd.backend.tasktracker.bot.session.UserSession;
import com.github.knkydd.backend.tasktracker.bot.session.state.StateFactory;
import com.github.knkydd.backend.tasktracker.bot.session.state.StateType;
import com.github.knkydd.backend.tasktracker.bot.session.state.UserState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class UpdateProcessor {

    private final CommandHandler commandHandler;

    private final SessionService sessionService;

    private final StateFactory factory;

    public void process(BotContext botContext) {
        long chatId = botContext.chatId();
        UserSession session = sessionService.getOrCreate(chatId);

        if (session.getStateType() != StateType.IDLE) {
            UserState state = factory.getUserState(session.getStateType());

            boolean accepted = state.handle(botContext, session);

            if (!accepted) {
                sessionService.reset(chatId);
            }
            if (state.getNextStateType().equals(StateType.IDLE)) {
                sessionService.reset(chatId);
            } else {
                session.setStateType(state.getNextStateType());
                sessionService.save(session);
            }

            return;
        }

        commandHandler.execute(botContext, session);
    }
}
