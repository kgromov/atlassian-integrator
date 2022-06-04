package com.atlassian.web.atlassianintegrator.jira.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@RequiredArgsConstructor
public class Settings {
    private final String url;
    private final String login;
    private final String password;
}
