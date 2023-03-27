package ru.tinkoff.edu.java.scrapper.web.webclient.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;

public record GithubRepositoryResponse(
        @JsonProperty("full_name") String fullName,
        @JsonProperty("updated_at") OffsetDateTime updatedAt
) {
}
