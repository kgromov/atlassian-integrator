package com.atlassian.web.atlassianintegrator.jira.service;

import com.atlassian.web.atlassianintegrator.jira.domain.Board;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.GenericType;
import java.util.List;

@Service
@EnableCaching
@RequiredArgsConstructor
public class BoardService {
    @Value("${jira.url.v1}")
    private String apiUrlV1;

    private final Client jiraClient;

    @SneakyThrows
    public List<Board> getBoards() {
        /*return jiraClient.target(apiUrlV1).path("board")
            .request(MediaType.APPLICATION_JSON_VALUE)
            .get().readEntity(new GenericType<List<Board>>() {});*/
        JsonNode boards = jiraClient.target(apiUrlV1).path("board")
            .request(MediaType.APPLICATION_JSON_VALUE)
            .get().readEntity(JsonNode.class).get("values");

        String content = boards.toString();
        return new ObjectMapper().readValue(content, new TypeReference<List<Board>>(){});
    }

    public List<Board> getBoardsByType(String type) {
        return jiraClient.target(apiUrlV1)
            .path("board")
            .queryParam("type", type)
            .request(MediaType.APPLICATION_JSON_VALUE)
            .get().readEntity(new GenericType<List<Board>>() {});
    }

    public Board getBoardByName(String name) {
        List<Board> boards = jiraClient.target(apiUrlV1)
            .path("board")
            .queryParam("name", name)
            .request(MediaType.APPLICATION_JSON_VALUE)
            .get().readEntity(new GenericType<List<Board>>() {
            });
        return boards.stream().findFirst().orElse(null);
    }
}
