package ru.tinkoff.edu.java.scrapper.domain.jpa.repository;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.tinkoff.edu.java.scrapper.domain.jpa.entity.Link;

import java.time.LocalDateTime;
import java.util.List;

@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jpa")
public interface JpaLinkRepository extends JpaRepository<Link, Long> {

    public Link findByUrl(String url);

    public List<Link> findAllByLastUpdateBefore(LocalDateTime time);
}
