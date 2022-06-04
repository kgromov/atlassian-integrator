package com.atlassian.web.atlassianintegrator.jira.config;

import com.atlassian.web.atlassianintegrator.bitbucket.config.BitbucketCredentials;
import lombok.RequiredArgsConstructor;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

@Configuration
@RequiredArgsConstructor
public class JiraClientProvider {
    private final Settings settings;

    @Bean
    public RestTemplate jiraRestTemplate() {
        return new RestTemplateBuilder()
            .basicAuthentication(settings.getLogin(), settings.getLogin())
            .build();
    }

    @Bean
    public RestTemplate bitbucketRestTemplate(BitbucketCredentials credentials) {
        return new RestTemplateBuilder()
            .basicAuthentication(credentials.getClientId(), credentials.getSecret())
            .build();
    }

    @Bean
    public Client jiraClient() {
        HttpAuthenticationFeature auth = HttpAuthenticationFeature.basic(settings.getLogin(), settings.getPassword());
        return ClientBuilder.newBuilder()
            .register(auth)
            .build();
    }
}
