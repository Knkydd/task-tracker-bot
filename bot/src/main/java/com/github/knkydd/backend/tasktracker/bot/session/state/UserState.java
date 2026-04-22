package com.github.knkydd.backend.tasktracker.bot.session.state;

import com.github.knkydd.backend.tasktracker.bot.session.UserSession;
import com.github.knkydd.backend.tasktracker.bot.telegram.BotContext;

public interface UserState {

    StateType getStateType();

    StateType getNextStateType();

    void handle(BotContext botContext, UserSession session);

}
