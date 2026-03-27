package com.github.knkydd.backend.tasktracker.bot.session;

import org.springframework.stereotype.Component;

@Component
public class IdValidator {

    public boolean isValidated(String maybeNumber){
        try{
            long taskId = Long.parseLong(maybeNumber);
        } catch (NumberFormatException e){
            return false;
        }
        return true;
    }

}
