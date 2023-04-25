package ru.tinkoff.edu.java.scrapper.dto.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jooq.impl.QOM;

import java.net.URI;
import java.time.OffsetDateTime;

@Getter
@Setter
@Accessors(chain = true)
public class Link {
    private long id;
    private URI url;
    private OffsetDateTime lastUpdate;
    private Integer answerCount;
    private Integer commentCount;
}
