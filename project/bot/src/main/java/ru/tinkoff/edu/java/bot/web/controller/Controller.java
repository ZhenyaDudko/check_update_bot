package ru.tinkoff.edu.java.bot.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.tinkoff.edu.java.bot.dto.web.LinkUpdateRequest;
import ru.tinkoff.edu.java.bot.update_handler.UpdateHandler;

@RestController
@RequiredArgsConstructor
public class Controller {

    private final UpdateHandler updateHandler;

    @PostMapping("updates/")
    public void linkUpdate(@RequestBody LinkUpdateRequest request) {
        updateHandler.handle(request);
    }

}
