package ru.tinkoff.edu.java.scrapper.domain.jpa.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Accessors(chain = true)
public class Chat {

    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST})
    @JoinTable(name = "chat_link",
            inverseJoinColumns = @JoinColumn(name = "link_id",
                    nullable = false),
            joinColumns = @JoinColumn(name = "chat_id",
                    nullable = false))
    private Set<Link> links = new HashSet<>();

}
