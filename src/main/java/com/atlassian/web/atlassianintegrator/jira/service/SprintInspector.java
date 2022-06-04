package com.atlassian.web.atlassianintegrator.jira.service;

import org.springframework.stereotype.Component;

@Component
public class SprintInspector {
    /*
     * Flow:
     * 1) get board by name
     * 2) find active or any sprint by name
     * 3) with quick filters aggregate issues by type and status
     * 4) introduce some intermediate field classes (e.g. issueType is not flat)
     * 5) get estimation as extra step (if needed)
     */
}
