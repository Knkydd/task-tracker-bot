package com.github.knkydd.backend.tasktracker.bot.property;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.convert.DurationUnit;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@ConfigurationProperties(prefix = "app.properties")
@Validated
@Getter
@Setter
@Component
public class TelegramProperty {

    @NotNull
    private String uri;

    @NotNull
    private String token;

    @DurationUnit(ChronoUnit.SECONDS)
    private Duration sleepUpdateListener;

}
