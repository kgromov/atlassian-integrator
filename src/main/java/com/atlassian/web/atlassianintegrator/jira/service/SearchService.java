package com.atlassian.web.atlassianintegrator.jira.service;

import com.atlassian.web.atlassianintegrator.jira.domain.Ticket;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import javax.ws.rs.client.Client;
import java.net.URLEncoder;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService {
    @Value("${jira.url.v2}")
    private String apiUrlV2;

    private final Client jiraClient;

    @SneakyThrows
    public List<Ticket> getIssuesByJql() {
        String url = jiraClient.target(apiUrlV2).path("search").getUri().toString();
        String query = convertJqlParamToUrlFormat("project=ESE " +
            "AND Sprint=2991 " +
            "AND issuetype in standardIssueTypes() " +
            "AND status in (\"To Do\", \"Ready\") " +
            "AND assignee in (EMPTY, \"thrni@eg.dk\", \"lasfr@eg.dk\")");
        String response = jiraClient.target(url + "?jql=" + query + "&fields=summary,issuetype")
            .request(MediaType.APPLICATION_JSON_VALUE)
            .get()
            .readEntity(String.class);
        return null;
    }

    @SneakyThrows
    private String convertJqlParamToUrlFormat(String jqlParam) {
        return URLEncoder.encode(jqlParam, "UTF-8").replaceAll("\\+", "%20");
    }
}
