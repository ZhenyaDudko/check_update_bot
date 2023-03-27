package ru.tinkoff.edu.java.scrapper.web.dto;

import org.hibernate.validator.constraints.URL;

public record AddLinkRequest(@URL String url) {
}
