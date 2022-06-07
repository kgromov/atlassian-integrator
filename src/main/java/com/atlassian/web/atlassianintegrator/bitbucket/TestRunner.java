package com.atlassian.web.atlassianintegrator.bitbucket;

import com.atlassian.web.atlassianintegrator.bitbucket.config.BitbucketCredentials;
import com.atlassian.web.atlassianintegrator.bitbucket.domain.PullRequestStatus;
import com.atlassian.web.atlassianintegrator.bitbucket.service.BitbucketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Slf4j
@Component
@RequiredArgsConstructor
public class TestRunner implements CommandLineRunner {
    private final BitbucketService bitbucketService;
    private final BitbucketCredentials credentials;

    @Override
    public void run(String... args) throws Exception {
        long count = bitbucketService.getPullRequestsCount(credentials, "xxrds", PullRequestStatus.MERGED, LocalDate.of(2022, 5, 20));
        log.info("Merged pull requests = {}", count);
    }
}
