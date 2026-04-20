package com.github.knkydd.backend.tasktracker.bot.session;

import com.github.knkydd.backend.tasktracker.bot.session.state.StateType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
@RequiredArgsConstructor
public class SessionService {

    private final Map<Long, UserSession> sessions = new ConcurrentHashMap<>();

    public UserSession getOrCreate(Long chatId) {
        return sessions.computeIfAbsent(chatId, UserSession::new);
    }

    public void save(UserSession userSession) {
        sessions.put(userSession.getChatId(), userSession);
    }

    public void reset(long chatId) {
        UserSession session = getOrCreate(chatId);
        session.setStateType(StateType.IDLE);
        session.clearTaskCategory();
        save(session);
    }
}
