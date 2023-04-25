package ru.tinkoff.edu.java.scrapper.domain.exception;

public class LinkNotFoundException extends RuntimeException {

    public LinkNotFoundException(String message) {
        super(message);
    }
}
