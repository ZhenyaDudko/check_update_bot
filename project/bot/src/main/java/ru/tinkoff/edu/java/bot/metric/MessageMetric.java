package ru.tinkoff.edu.java.bot.metric;

import io.micrometer.core.instrument.Metrics;

public class MessageMetric {

    private MessageMetric() {
        throw new IllegalStateException("Utility class");
    }

    public static void incrementMessageCount() {
        Metrics.counter("message_count").increment();
    }
}
