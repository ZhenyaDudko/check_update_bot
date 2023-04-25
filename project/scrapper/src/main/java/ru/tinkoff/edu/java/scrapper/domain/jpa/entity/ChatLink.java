package ru.tinkoff.edu.java.scrapper.domain.jpa.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@IdClass(ChatLinkId.class)
@Table(name = "chat_link")
public class ChatLink {

    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @Column(name = "chat_id", nullable = false)
    private Long chatId;

    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @Column(name = "link_id", nullable = false)
    private Long linkId;

}
