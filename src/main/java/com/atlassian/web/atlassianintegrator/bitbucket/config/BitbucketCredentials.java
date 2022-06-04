package com.atlassian.web.atlassianintegrator.bitbucket.config;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.stereotype.Component;


@Component
@Getter
@RequiredArgsConstructor
@EqualsAndHashCode(of = {"clientId", "secret"})
@ToString
@ConstructorBinding
@ConfigurationProperties(prefix = "bitbucket")
public class BitbucketCredentials {
    private final String clientId;
    private final String secret;
    private final String apiUrl;
    private final String authorizeUrl;
}
