package ru.tinkoff.edu.java.scrapper.domain.jpa.repository;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.tinkoff.edu.java.scrapper.domain.jpa.entity.Link;

@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jpa")
public interface JpaLinkRepository extends JpaRepository<Link, Long> {

    Link findByUrl(String url);

    List<Link> findAllByLastUpdateBefore(LocalDateTime time);
}
