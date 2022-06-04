package com.atlassian.web.atlassianintegrator.bitbucket.service;

import com.atlassian.web.atlassianintegrator.bitbucket.config.BitbucketCredentials;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Configuration
public class RestTemplateConfiguration {

    @Bean
    @Qualifier("notAuthorized")
    public RestTemplate createNotAuthorizedRestTemplate(BitbucketCredentials credentials) {
        return new RestTemplateBuilder()
            .basicAuthentication(credentials.getClientId(), credentials.getSecret())
//            .defaultHeader(CONTENT_TYPE, APPLICATION_FORM_URLENCODED_VALUE)
            .build();
    }

    @Bean
    @Qualifier("authorized")
    public RestTemplate createAuthorizedRestTemplate() {
        return new RestTemplateBuilder()
            .defaultHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
            .build();
    }

}
