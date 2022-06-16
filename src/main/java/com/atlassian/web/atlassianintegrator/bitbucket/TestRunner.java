package com.atlassian.web.atlassianintegrator.bitbucket;

import com.atlassian.web.atlassianintegrator.bitbucket.config.BitbucketCredentials;
import com.atlassian.web.atlassianintegrator.bitbucket.domain.PullRequestStatus;
import com.atlassian.web.atlassianintegrator.bitbucket.service.BitbucketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class TestRunner implements CommandLineRunner {
    private final BitbucketService bitbucketService;
    private final BitbucketCredentials credentials;

    @Override
    public void run(String... args) throws Exception {
        LocalDate from = LocalDate.of(2022, 6, 2);
        LocalDate to = from.plusDays(14);
        List<String> users = Arrays.asList("xxetu", "xxigb", "xxrds", "xxmmi", "vcheb", "olesh");
        users.parallelStream()
                .forEach(user -> {
                    long count = bitbucketService.getPullRequestsCount(credentials, user, PullRequestStatus.MERGED, from, to);
                    log.info("{} merged {} pull requests", user, count);
                });
    }
}
