package ru.tinkoff.edu.java.scrapper.domain.jpa.entity;

import java.io.Serializable;

public class ChatLinkId implements Serializable {

    private Long chatId;

    private Long linkId;

    public ChatLinkId() {}

    public ChatLinkId(Long chatId, Long linkId) {
        this.chatId = chatId;
        this.linkId = linkId;
    }
}
