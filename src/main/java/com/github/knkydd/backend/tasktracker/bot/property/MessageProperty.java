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
    private String view;

    @NotNull
    private String viewTemplate;

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
    }

    @NotNull
    private CompleteTask completeTask;

    @Getter
    @Setter
    @Valid
    public static class CompleteTask {
        @NotNull
        private String processComplete;

        @NotNull
        private String completeSuccess;
    }

    @NotNull
    private DeleteTask deleteTask;

    @Getter
    @Setter
    @Valid
    public static class DeleteTask {
        @NotNull
        private String processDelete;

        @NotNull
        private String completeDelete;
    }

    @NotNull
    private Errors errors;

    @Getter
    @Setter
    @Valid
    public static class Errors {

        @NotNull
        private AddErrors addErrors;

        @NotNull
        private ViewErrors viewErrors;

        @NotNull
        private CompleteErrors completeErrors;

        @NotNull
        private DeleteErrors deleteErrors;

        @NotNull
        private IdValidateErrors idValidateErrors;

        @NotNull
        private String unknownError;

        @Getter
        @Setter
        @Valid
        public static class AddErrors {

            @NotNull
            private String categoryValidate;

            @NotNull
            private String CategorySave;

            @NotNull
            private String DescriptionValidate;

            @NotNull
            private String DescriptionSaveUser;

            @NotNull
            private String DescriptionSaveTask;
        }

        @Getter
        @Setter
        @Valid
        public static class ViewErrors {
            private String gettingTasks;
        }

        @Getter
        @Setter
        @Valid
        public static class CompleteErrors {

            @NotNull
            private String dbDelete;
        }

        @Getter
        @Setter
        @Valid
        public static class DeleteErrors {

            @NotNull
            private String dbDelete;
        }

        @Getter
        @Setter
        @Valid
        public static class IdValidateErrors {

            @NotNull
            private String notANumber;

            @NotNull
            private String taskNotExists;
        }
    }

    @NotNull
    private String unknown;
}
