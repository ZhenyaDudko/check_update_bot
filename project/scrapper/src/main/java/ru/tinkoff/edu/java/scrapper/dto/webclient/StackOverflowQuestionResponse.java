package ru.tinkoff.edu.java.scrapper.dto.webclient;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public record StackOverflowQuestionResponse(OffsetDateTime updatedAt, String title, Integer answerCount,
                                            Integer commentCount) {

    @JsonCreator
    public StackOverflowQuestionResponse(@JsonProperty("creation_date") long creationDate,
                                         @JsonProperty("last_edit_date") long lastEditDate,
                                         @JsonProperty("title") String title,
                                         @JsonProperty("answer_count") Integer answerCount) {
        this(OffsetDateTime.of(
                        LocalDateTime.ofEpochSecond(
                                lastEditDate == 0 ? creationDate : lastEditDate,
                                0, ZoneOffset.UTC
                        ), ZoneOffset.UTC),
                title, answerCount, 0);
    }
}
