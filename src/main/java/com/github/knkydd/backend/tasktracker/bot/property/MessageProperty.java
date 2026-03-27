package com.github.knkydd.backend.tasktracker.bot.property;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "app.messages")
@Getter
@Setter
@Validated
@Component
public class MessageProperty {

    @NotNull
    private String start;

    @NotNull
    private String help;

    @NotNull
    private AddTask addTask;

    @Getter
    @Setter
    @Valid
    public static class AddTask {
        @NotNull
        private String addCategory;

        @NotNull
        private String addDescription;

        @NotNull
        private String completeAdd;

        @NotNull
        private String error;
    }

    @NotNull
    private CompleteTask completeTask;

    @Getter
    @Setter
    @Valid
    public static class CompleteTask {
        @NotNull
        private String process;

        @NotNull
        private String complete;
    }

    @NotNull
    private DeleteTask deleteTask;

    @Getter
    @Setter
    @Valid
    public static class DeleteTask {
        @NotNull
        private String delete;

        @NotNull
        private String complete;
    }

    @NotNull
    private String unknown;
}
