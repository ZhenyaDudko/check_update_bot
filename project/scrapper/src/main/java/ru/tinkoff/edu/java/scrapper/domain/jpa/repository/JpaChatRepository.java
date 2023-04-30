package ru.tinkoff.edu.java.scrapper.domain.jpa.repository;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.tinkoff.edu.java.scrapper.domain.jpa.entity.Chat;

@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jpa")
public interface JpaChatRepository extends JpaRepository<Chat, Long> {
}
