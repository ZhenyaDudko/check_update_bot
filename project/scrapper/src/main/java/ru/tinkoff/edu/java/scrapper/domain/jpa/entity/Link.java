package ru.tinkoff.edu.java.scrapper.domain.jpa.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Link {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "url", unique = true, nullable = false)
    private String url;

    @Column(name = "last_update", nullable = false)
    private LocalDateTime lastUpdate = LocalDateTime.now();

    @Column(name = "comment_count")
    private Integer commentCount = null;

    @Column(name = "answer_count")
    private Integer answerCount = null;

    @OneToMany(mappedBy = "linkId", fetch = FetchType.LAZY)
    private List<ChatLink> chatLinks = new ArrayList<>();

}
