package ru.tinkoff.edu.java.scrapper.dto.domain;

import org.jooq.Record;
import org.jooq.RecordMapper;
import ru.tinkoff.edu.java.scrapper.domain.jooq.data.tables.records.LinkRecord;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.ZoneOffset;

public class LinkRecordMapper implements RecordMapper<LinkRecord, Link> {

    @Override
    public Link map(LinkRecord linkRecord) {
        try {
            return new Link().setId(linkRecord.getId()).setUrl(new URI(linkRecord.getUrl()))
                    .setLastUpdate(linkRecord.getLastUpdate().atOffset(ZoneOffset.UTC))
                    .setAnswerCount(linkRecord.getAnswerCount())
                    .setCommentCount(linkRecord.getCommentCount());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
