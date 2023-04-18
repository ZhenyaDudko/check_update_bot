package ru.tinkoff.edu.java.scrapper.dto.controller;

import jakarta.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.URL;

public record AddLinkRequest(@URL @NotEmpty String url) {
}
