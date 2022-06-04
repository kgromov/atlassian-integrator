package com.atlassian.web.atlassianintegrator.jira.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
// TODO: split bugs and tasks; introduce PO, DEV, QA lists
public enum CommonQuickFilters implements QuickFilters{
    READY_FOR_WORK("project = ESE " +
                       "AND issuetype in (Bug, Task) " +
                       "AND status in (\"To Do\", \"Ready\") " +
                       "AND assignee in (EMPTY, \"thrni@eg.dk\", \"lasfr@eg.dk\")"),
    UNDER_DEVELOPMENT("project = ESE " +
        "AND issuetype in (Bug, Task) " +
        "AND status = \"In Progress\" " +
        "AND assignee in (\"xxigb@eg.dk\", \"xxetu@eg.dk\", \"xxmmi@eg.dk\", \"xxdma@eg.dk\", \"xxxyo@eg.dk\", \"xxrds@eg.dk\")"),
    IN_QA("project = ESE " +
        "AND issuetype in (Bug, Task)) " +
        "AND status = \"In Progress\" " +
        "AND assignee not in (\"xxigb@eg.dk\", \"xxetu@eg.dk\", \"xxmmi@eg.dk\", \"xxdma@eg.dk\", \"xxxyo@eg.dk\", \"xxrds@eg.dk\")"),
    READY_FOR_REVIEW("project = ESE " +
        "AND issuetype in (Bug, Task) " +
        "AND status = \"Ready for review\"  " +
        "AND assignee in (\"thrni@eg.dk\", \"runan@eg.dk\", \"bkern@eg.dk\", \"lasfr@eg.dk\")");

    private final String jql;
}
