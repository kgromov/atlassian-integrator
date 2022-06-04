package com.atlassian.web.atlassianintegrator;

import com.atlassian.web.atlassianintegrator.bitbucket.config.BitbucketCredentials;
import com.atlassian.web.atlassianintegrator.bitbucket.config.BitbucketJwtToken;
import com.atlassian.web.atlassianintegrator.bitbucket.domain.PullRequestStatus;
import com.atlassian.web.atlassianintegrator.bitbucket.service.AuthorizationService;
import com.atlassian.web.atlassianintegrator.bitbucket.service.BitbucketService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@EnableConfigurationProperties(BitbucketCredentials.class)
public class AtlassianIntegratorApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(AtlassianIntegratorApplication.class, args);
        AuthorizationService authorizationService = context.getBean(AuthorizationService.class);
        BitbucketCredentials credentials = context.getBean(BitbucketCredentials.class);
        BitbucketService bitbucketService = context.getBean(BitbucketService.class);

        bitbucketService.getPullRequests(credentials, "xxrds", PullRequestStatus.DECLINED);

//        BitbucketJwtToken token = authorizationService.authorize(credentials);
    }
}
