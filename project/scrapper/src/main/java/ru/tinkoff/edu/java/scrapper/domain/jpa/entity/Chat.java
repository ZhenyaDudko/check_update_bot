package ru.tinkoff.edu.java.scrapper.domain.jpa.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Chat {

    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @OneToMany(mappedBy = "chatId", fetch = FetchType.LAZY)
    private List<ChatLink> chatLinks = new ArrayList<>();

}
