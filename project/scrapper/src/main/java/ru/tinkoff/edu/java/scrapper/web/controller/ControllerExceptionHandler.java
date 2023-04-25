package ru.tinkoff.edu.java.scrapper.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.tinkoff.edu.java.scrapper.domain.exception.ChatNotFoundException;
import ru.tinkoff.edu.java.scrapper.domain.exception.LinkNotFoundException;
import ru.tinkoff.edu.java.scrapper.dto.controller.ApiErrorResponse;

import java.net.URISyntaxException;
import java.util.Arrays;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler({
            MethodArgumentNotValidException.class,
            HttpMessageNotReadableException.class,
            URISyntaxException.class,
            IllegalArgumentException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse badRequestException(Exception e) {
        return createError(e, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ChatNotFoundException.class, LinkNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiErrorResponse bdException(Exception e) {
        return createError(e, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiErrorResponse internalException(Exception e) {
        return createError(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ApiErrorResponse createError(Exception e, HttpStatus status) {
        return new ApiErrorResponse(
                status.getReasonPhrase(),
                String.valueOf(status.value()),
                e.getClass().getSimpleName(),
                e.getMessage(),
                Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).toList()
        );
    }
}
