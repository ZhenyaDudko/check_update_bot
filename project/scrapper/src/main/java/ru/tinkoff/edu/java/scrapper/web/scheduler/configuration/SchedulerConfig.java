package ru.tinkoff.edu.java.scrapper.web.scheduler.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.edu.java.scrapper.configuration.ApplicationConfig;

@Configuration
public class SchedulerConfig {

    @Bean
    public long schedulerIntervalMs(ApplicationConfig config) {
        return config.scheduler().interval().toMillis();
    }
}
