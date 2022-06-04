package com.atlassian.web.atlassianintegrator.jira.service;

import com.atlassian.web.atlassianintegrator.jira.config.Settings;
import com.atlassian.web.atlassianintegrator.jira.domain.Board;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import javax.ws.rs.client.Client;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

@Component
public class Authentication {
    @Value("${jira.url.v1}")
    private String apiUrlV1;
    @Value("${jira.url.v2}")
    private String apiUrlV2;

    @Autowired
    private Settings settings;
    @Autowired
    private Client jiraClient;
    @Autowired
    private BoardService boardService;

    public void login() {

        String boardsResponse = jiraClient.target(apiUrlV1).path("board")
            .request(MediaType.APPLICATION_JSON_VALUE)
            .get().readEntity(String.class);
        List<Board> boards = boardService.getBoards();

        String url = jiraClient.target(apiUrlV2).getUri().toString();
        String query = convertJqlParamToUrlFormat("project = ESE " +
            "AND issuetype in standardIssueTypes() " +
            "AND status in (\"To Do\", \"Ready\") " +
            "AND assignee in (EMPTY, \"thrni@eg.dk\", \"lasfr@eg.dk\")");
        String response = jiraClient.target(url + "?jql=" + query + "&fields=summary,type")
            .request(MediaType.APPLICATION_JSON_VALUE)
            .get().readEntity(String.class);

        jiraClient.target(apiUrlV1).path("board").path("202").path("sprint").path("2725").path("issue")
            .queryParam("fields", "summary,customfield_10106,issuetype")
            .request(MediaType.APPLICATION_JSON_VALUE)
            .get().readEntity(String.class);
        int a = 1;


    }

    private String convertJqlParamToUrlFormat(String jqlParam) {
        try {
            return URLEncoder.encode(jqlParam, "UTF-8").replaceAll("\\+", "%20");
        } catch (UnsupportedEncodingException var3) {
            return "";
        }
    }
}
