package com.atlassian.web.atlassianintegrator.bitbucket.service;

import com.atlassian.web.atlassianintegrator.bitbucket.config.BitbucketCredentials;
import com.atlassian.web.atlassianintegrator.bitbucket.config.BitbucketJwtToken;
import com.atlassian.web.atlassianintegrator.bitbucket.domain.PullRequestStatus;
import lombok.SneakyThrows;
import org.glassfish.jersey.internal.guava.CacheBuilder;
import org.glassfish.jersey.internal.guava.CacheLoader;
import org.glassfish.jersey.internal.guava.LoadingCache;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Service
public class BitbucketService {
    private final AuthorizationService authorizationService;
    private final RestTemplate restTemplate;

    private final LoadingCache<BitbucketCredentials, BitbucketJwtToken> cachedTokens = CacheBuilder.newBuilder()
        .expireAfterAccess(7200, TimeUnit.MILLISECONDS)
        .build(new CacheLoader<BitbucketCredentials, BitbucketJwtToken>() {
            @Override
            public BitbucketJwtToken load(BitbucketCredentials credentials) {
                return authorizationService.authorize(credentials);
            }
        });

    public BitbucketService(AuthorizationService authorizationService, @Qualifier("authorized") RestTemplate restTemplate) {
        this.authorizationService = authorizationService;
        this.restTemplate = restTemplate;
    }

    // seems no way?
    public void getUsers(BitbucketCredentials credentials) {
        restTemplate.getInterceptors().add(tokenInterceptor(credentials));

        ResponseEntity<String> response = restTemplate.getForEntity(URI.create(credentials.getApiUrl() + "/workspaces/eg_as/members"), String.class);
        response.getBody();

    }

    // TODO: see for projection here - https://developer.atlassian.com/cloud/bitbucket/rest/intro/#partial-response
    // https://bitbucket.org/api/2.0/repositories/YOUR_NAME/REPO_NAME/pullrequests
    // https://api.bitbucket.org/2.0/repositories/USER_NAME/REPO_SLUG/pullrequests?q=updated_on="YYYY-MM-DD HH:MM[:ss[.uuuuuu]][TZ]"
    public void getPullRequests(BitbucketCredentials credentials, String user, PullRequestStatus status) {
        restTemplate.getInterceptors().add(tokenInterceptor(credentials));

//        ResponseEntity<String> response = restTemplate.getForEntity(URI.create(credentials.getApiUrl() + "/pullrequests/xxrds?state=DECLINED"), String.class);
        ResponseEntity<String> response = restTemplate.getForEntity(URI.create(credentials.getApiUrl() + "/pullrequests/" + user + "?state=" + status), String.class);
        response.getBody();
    }

    private ClientHttpRequestInterceptor tokenInterceptor(BitbucketCredentials credentials) {
        return (request, bytes, execution) -> {
            try {
                BitbucketJwtToken token = cachedTokens.get(credentials);
                request.getHeaders().add("Authorization", "Bearer " + token.getAccess_token());
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
            return execution.execute(request, bytes);
        };
    }
}
