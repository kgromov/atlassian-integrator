package com.atlassian.web.atlassianintegrator.jira.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum StoryQuickFilters implements QuickFilters{
    READY_FOR_WORK("project = ESE " +
        "AND issuetype in standardIssueTypes()  " +
        "AND status in (\"To Do\", \"Ready\") " +
        "AND assignee in (EMPTY, \"thrni@eg.dk\", \"lasfr@eg.dk\")"),
    UNDER_DEVELOPMENT("project = ESE AND ((issuetype in subTaskIssueTypes() AND text ~ DEV) OR issuetype in (Bug, Task)) " +
        "AND status = \"In Progress\" " +
        "AND assignee in (\"xxigb@eg.dk\", \"xxetu@eg.dk\", \"xxmmi@eg.dk\", \"xxdma@eg.dk\", \"xxxyo@eg.dk\", \"xxrds@eg.dk\")"),
    WAITING_FOR_CODE_REVIEW("project = ESE " +
        "AND issuetype in subTaskIssueTypes() " +
        "AND status = \"Ready for review\"  AND \"Team name\" = UA1 " +
        "AND text ~ DEV ORDER BY created ASC"),
    READY_FOR_QA("project = ESE " +
        "AND issuetype in subTaskIssueTypes() " +
        "AND status in (\"To Do\", \"Ready\") " +
        "AND text ~ QA AND assignee !=EMPTY"),
    IN_QA("project = ESE " +
        "AND issuetype in subTaskIssueTypes() " +
        "AND status = \"In Progress\" " +
        "AND \"Team name\" = UA1 AND text ~ QA ORDER BY created DESC"),
    // doubts
    WAITING_FOR_BUGFIXES("project = ESE " +
        "AND issuetype in subTaskIssueTypes() " +
        "AND status = \"Ready for review\" " +
        "AND \"Team name\" = UA1 AND text ~ QA " +
        "ORDER BY created DESC"),
    READY_FOR_REVIEW("project = ESE " +
        "AND issuetype in standardIssueTypes()  " +
        "AND status = \"Ready for review\"  " +
        "AND assignee in (\"thrni@eg.dk\", \"runan@eg.dk\", \"bkern@eg.dk\", \"lasfr@eg.dk\")"),
    BLOCKED("project = ESE " +
        "AND Flagged = Impediment " +
        "AND \"Team name\" = UA1 " +
        "ORDER BY created DESC"),
    DONE("project = ESE " +
        "AND issuetype in standardIssueTypes() " +
        "AND status = \"Done\" " +
        "ORDER by issuetype DESC");

    private final String jql;
}
