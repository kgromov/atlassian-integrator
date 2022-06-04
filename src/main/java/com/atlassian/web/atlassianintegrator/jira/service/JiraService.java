package com.atlassian.web.atlassianintegrator.jira.service;

import com.atlassian.web.atlassianintegrator.jira.domain.Sprint;
import com.atlassian.web.atlassianintegrator.jira.domain.Ticket;

import java.util.List;

public interface JiraService {

    Sprint getActiveSprint();

    // by some criteria (at least project; team etc)?
    List<Sprint> getSprints();

    Ticket getTicketById(Sprint id);

    List<Ticket> getTicketByQuery(String jql);

}
