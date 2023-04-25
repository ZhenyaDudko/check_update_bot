package ru.tinkoff.edu.java.scrapper.domain.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.tinkoff.edu.java.scrapper.domain.jpa.entity.Link;

public interface JpaChatRepository extends JpaRepository<Link, Long> {
}
