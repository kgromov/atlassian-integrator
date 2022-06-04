package com.atlassian.web.atlassianintegrator.jira.service;

import com.atlassian.web.atlassianintegrator.jira.domain.Sprint;
import com.atlassian.web.atlassianintegrator.jira.domain.Ticket;

import java.util.List;

// TODO: as next steps Jira service could be implemented with the help of Session or smth similar

/**
 * All info could retrieved or small details per issue:
 * Issue overview:      https://jira.eg.dk/rest/api/2/issuetype/10001
 * Issue details:       https://jira.eg.dk/rest/api/2/issue/ESE-2589/:
 * project,
 * customfield_10105 - active sprint
 * customfield_10216 - team
 * subtasks
 * customfield_10000 - PR
 * sprints          - https://jira.eg.dk/rest/agile/1.0/board/202/sprint
 * https://jira.eg.dk/rest/agile/1.0/board - list all boards
 * issues in sprint (with details):
 * https://jira.eg.dk/rest/agile/1.0/sprint/1126/issue?maxResults=100
 * ?fields=project or rest fields
 * https://jira.eg.dk/rest/agile/1.0/issue/85303
 *
 * does not work for some reason :(
 * agile/1.0/board/{boardId}/quickfilter
 * agile/1.0/board/{boardId}/quickfilter/{quickFilterId}
 *
 * timetracking - time spent total; hours
 * worklog
 * customfield_10106 - story points
 *
 *
 * Project details:     https://jira.eg.dk/rest/api/2/project/10711     // could be retrieved from Issue details (fields -> project)
 *
 */
public class SimpleJiraService implements JiraService {

    @Override
    public Sprint getActiveSprint() {
        // + get values
        /*return jiraClient.target(apiUrlV1).path("board").path("202").path("sprint")
            .queryParam("state", "active")
            .request(MediaType.APPLICATION_JSON_VALUE)
            .get().readEntity(String.class);*/
        return null;
    }

    @Override
    public List<Sprint> getSprints() {
        return null;
    }

    @Override
    public Ticket getTicketById(Sprint id) {
        return null;
    }

    @Override
    //search?jql=
    public List<Ticket> getTicketByQuery(String jql) {
        return null;
    }
}
