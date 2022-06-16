package com.atlassian.web.atlassianintegrator.bitbucket.controllers;

import com.atlassian.web.atlassianintegrator.bitbucket.config.BitbucketCredentials;
import com.atlassian.web.atlassianintegrator.bitbucket.domain.PullRequestStatus;
import com.atlassian.web.atlassianintegrator.bitbucket.service.BitbucketService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("pullRequests")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:9000")
public class PullRequestController {
    private final BitbucketService bitbucketService;
    private final BitbucketCredentials credentials;

    @GetMapping("/{user}/{status}")
    public long getPullRequestCount(@PathVariable String user,
                                    @PathVariable PullRequestStatus status,
                                    @RequestParam String from,
                                    @RequestParam String to) {
        LocalDate fromDate = LocalDate.parse(from);
        LocalDate toDate = LocalDate.parse(to);
        return bitbucketService.getPullRequestsCount(credentials, user, status, fromDate, toDate);
    }
}
