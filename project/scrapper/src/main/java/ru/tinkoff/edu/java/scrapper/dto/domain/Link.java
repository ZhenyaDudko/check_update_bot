package ru.tinkoff.edu.java.scrapper.dto.domain;

import java.net.URI;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

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
