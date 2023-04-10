package ru.tinkoff.edu.java.bot.exceptions.web;

public class IncorrectRequestParametersException extends Exception {
    public IncorrectRequestParametersException(String message) {
        super(message);
    }

    public IncorrectRequestParametersException() {
        super();
    }
}
