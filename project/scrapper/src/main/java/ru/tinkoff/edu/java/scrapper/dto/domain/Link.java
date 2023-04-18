package ru.tinkoff.edu.java.scrapper.dto.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.net.URI;
import java.time.OffsetDateTime;

@Getter
@Setter
@Accessors(chain = true)
public class Link {
    private long id;
    private URI url;
    private OffsetDateTime lastUpdate;
    private long answerCount;
    private long commentCount;
}
