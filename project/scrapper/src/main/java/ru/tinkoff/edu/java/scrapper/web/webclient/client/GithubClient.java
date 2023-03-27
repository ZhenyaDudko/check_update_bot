package ru.tinkoff.edu.java.scrapper.web.webclient.client;

import ru.tinkoff.edu.java.scrapper.web.webclient.dto.GithubRepositoryResponse;

public interface GithubClient {

    GithubRepositoryResponse fetchRepository(String user, String repository);
}
