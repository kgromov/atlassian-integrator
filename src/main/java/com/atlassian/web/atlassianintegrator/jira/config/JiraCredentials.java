package com.atlassian.web.atlassianintegrator.jira.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JiraCredentials {
    @Value("${jira.url}")
    private String url;
    @Value("${jira.login}")
    private String login;
    @Value("${jira.password}")
    private String password;

    @Bean
    public Settings settings() {
        return new Settings(url, login, password);
    }
}
