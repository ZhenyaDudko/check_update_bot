package ru.tinkoff.edu.java.scrapper.dto.controller;

import jakarta.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.URL;

public record RemoveLinkRequest(@URL @NotEmpty String url) {
}
