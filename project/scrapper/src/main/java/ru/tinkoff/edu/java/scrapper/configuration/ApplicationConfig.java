package ru.tinkoff.edu.java.scrapper.configuration;

import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.boot.context.properties.bind.Name;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationConfig(
        @NotNull String test,
        Scheduler scheduler,
        @Name("database-access-type") AccessType accessType,
        @DefaultValue("queue") String queueName,
        @DefaultValue("exchange") String exchangeName,
        boolean useQueue
) {

    public record Scheduler(Duration interval) {
    }

    public enum AccessType {JDBC, JPA, JOOQ}

}
