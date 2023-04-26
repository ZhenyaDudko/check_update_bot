package ru.tinkoff.edu.java.scrapper.domain.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.tinkoff.edu.java.scrapper.domain.jpa.entity.Chat;

public interface JpaChatRepository extends JpaRepository<Chat, Long> {
}
