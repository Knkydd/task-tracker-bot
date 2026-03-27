package com.github.knkydd.backend.tasktracker.bot.session.state;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class StateFactory {

    private final Map<StateType, UserState> states;

    public StateFactory(List<UserState> userStates){
        this.states = userStates.stream().collect(Collectors.toMap(UserState::getStateType, s->s));
    }

    public UserState getUserState(StateType stateType){
        return states.get(stateType);
    }
}
