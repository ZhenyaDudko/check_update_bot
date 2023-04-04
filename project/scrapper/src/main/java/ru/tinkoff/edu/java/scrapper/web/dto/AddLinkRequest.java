package ru.tinkoff.edu.java.scrapper.web.dto;

import jakarta.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.URL;

public record AddLinkRequest(@URL @NotEmpty String url) {
}
