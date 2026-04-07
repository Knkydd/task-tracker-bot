package com.github.knkydd.backend.tasktracker.bot.session;

import com.github.knkydd.backend.tasktracker.bot.session.state.StateType;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserSession {

    private long chatId;

    private StateType stateType = StateType.IDLE;

    private String category;

    public UserSession(long chatId) {
        this.chatId = chatId;
    }

    public void clearTaskCategory() {
        this.category = null;
    }

}
