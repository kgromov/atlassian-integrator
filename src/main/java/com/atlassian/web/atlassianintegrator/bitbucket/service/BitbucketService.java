package com.atlassian.web.atlassianintegrator.bitbucket.service;

import com.atlassian.web.atlassianintegrator.bitbucket.config.BitbucketCredentials;
import com.atlassian.web.atlassianintegrator.bitbucket.config.BitbucketJwtToken;
import lombok.RequiredArgsConstructor;
import org.glassfish.jersey.internal.guava.CacheBuilder;
import org.glassfish.jersey.internal.guava.CacheLoader;
import org.glassfish.jersey.internal.guava.LoadingCache;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class BitbucketService {
    private final LoginService loginService;

    private final LoadingCache<BitbucketCredentials, BitbucketJwtToken> cachedTokens = CacheBuilder.newBuilder()
        .expireAfterAccess(7200, TimeUnit.MILLISECONDS)
        .build(new CacheLoader<BitbucketCredentials, BitbucketJwtToken>() {
            @Override
            public BitbucketJwtToken load(BitbucketCredentials credentials) {
                String securityCode = loginService.getSecurityCode(credentials);
                return loginService.authorize(credentials, securityCode);
            }
        });

}
