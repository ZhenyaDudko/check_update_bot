package ru.tinkoff.edu.java.bot.web.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.tinkoff.edu.java.bot.web.dto.LinkUpdateRequest;

@RestController
public class Controller {

    @PostMapping("updates/")
    public void linkUpdate(@RequestBody LinkUpdateRequest request) {}

}
