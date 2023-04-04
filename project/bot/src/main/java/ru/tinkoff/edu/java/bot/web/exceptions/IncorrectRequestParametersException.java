package ru.tinkoff.edu.java.bot.web.exceptions;

public class IncorrectRequestParametersException extends Exception {
    public IncorrectRequestParametersException(String message) {
        super(message);
    }

    public IncorrectRequestParametersException() {
        super();
    }
}
