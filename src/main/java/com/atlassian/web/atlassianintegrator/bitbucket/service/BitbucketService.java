package com.atlassian.web.atlassianintegrator.bitbucket.service;

import com.atlassian.web.atlassianintegrator.bitbucket.config.BitbucketCredentials;
import com.atlassian.web.atlassianintegrator.bitbucket.config.BitbucketJwtToken;
import com.atlassian.web.atlassianintegrator.bitbucket.domain.PullRequestStatus;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.glassfish.jersey.internal.guava.CacheBuilder;
import org.glassfish.jersey.internal.guava.CacheLoader;
import org.glassfish.jersey.internal.guava.LoadingCache;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@Service
public class BitbucketService {
    public static final String UPDATED_ON_DATE_TIME_PATTERN = "YYYY-MM-dd'T'HH:mm:ss";
    private final AuthorizationService authorizationService;
    private final RestTemplate restTemplate;
    // TODO: add with more configurable cache
    private final LoadingCache<BitbucketCredentials, BitbucketJwtToken> cachedTokens = CacheBuilder.newBuilder()
        .expireAfterAccess(7200, TimeUnit.MILLISECONDS)
        .build(new CacheLoader<BitbucketCredentials, BitbucketJwtToken>() {
            @Override
            public BitbucketJwtToken load(BitbucketCredentials credentials) {
                log.info("Getting authorization token");
                BitbucketJwtToken jwtToken = authorizationService.authorize(credentials);
                log.info("{}", jwtToken);
                return jwtToken;
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
    // source.repository.full_name != "main/repo" AND state = "OPEN" AND reviewers.nickname = "evzijst" AND destination.branch.name = "master" =>
    // /2.0/repositories/main/repo/issues?q=%28state+%3D+%22new%22+OR+state+%3D+%22on+hold%22%29+AND+assignee+%3D+null+AND+component+%3D+%22UI%22+and+updated_on+%3E+2015-11-11T00%3A00%3A00-07%3A00
    // restTemplate.getForEntity(URI.create(credentials.getApiUrl() + "/pullrequests/" + user + "?fields=values.author.nickname,values.author.display_name,size&state=MERGED&q=updated_on+%3E+2022-05-20T00%3A00%3A00"), String.class)
    public void getPullRequests(BitbucketCredentials credentials, String user, PullRequestStatus status) {
        restTemplate.getInterceptors().add(tokenInterceptor(credentials));

//        ResponseEntity<String> response = restTemplate.getForEntity(URI.create(credentials.getApiUrl() + "/pullrequests/xxrds?state=DECLINED"), String.class);
        ResponseEntity<String> response = restTemplate.getForEntity(URI.create(credentials.getApiUrl() + "/pullrequests/" + user + "?state=" + status), String.class);
        response.getBody();
    }

    public void getDefaultReviewers(BitbucketCredentials credentials, String repoName) {
        restTemplate.getInterceptors().add(tokenInterceptor(credentials));
        ResponseEntity<String> response = restTemplate.getForEntity(URI.create(credentials.getApiUrl() + "/pullrequests/eg_as/"+repoName+"/default-reviewers"), String.class);
        response.getBody();
    }

    @SneakyThrows
    public long getPullRequestsCount(BitbucketCredentials credentials, String user, PullRequestStatus status, LocalDate from, LocalDate to) {
        RestTemplate restTemplate = new RestTemplateBuilder()
                .defaultHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .interceptors(tokenInterceptor(credentials)/*, new LoggingRequestInterceptor()*/)
                .build();

        String query = new StringBuilder()
                .append("state=").append("\"").append(status).append("\"")
                .append(" AND ")
                .append("updated_on").append(">").append(from.atStartOfDay().format(DateTimeFormatter.ofPattern(UPDATED_ON_DATE_TIME_PATTERN)))
                .append(" AND ")
                .append("updated_on").append("<").append(to.atTime(LocalTime.MAX).format(DateTimeFormatter.ofPattern(UPDATED_ON_DATE_TIME_PATTERN)))
                .toString();
        String params = "?q=" + URLEncoder.encode(query, "utf-8") + "&fields=size";
        ResponseEntity<JsonNode> response = restTemplate.getForEntity(URI.create(credentials.getApiUrl() + "/pullrequests/" + user + params), JsonNode.class);
        try {
            log.info("Response: {}", response.getBody().toPrettyString());
        } catch (Exception e) {
            BitbucketJwtToken token = cachedTokens.get(credentials);
            authorizationService.refreshToken(credentials, token.getRefresh_token());
            response = restTemplate.getForEntity(URI.create(credentials.getApiUrl() + "/pullrequests/" + user + params), JsonNode.class);
            log.info("Response: {}", response.getBody().toPrettyString());
        }
        return response.getBody().get("size").asLong(0);
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

    private static class LoggingRequestInterceptor implements ClientHttpRequestInterceptor {

        @Override
        public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
            traceRequest(request, body);
            return execution.execute(request, body);
        }

        private void traceRequest(HttpRequest request, byte[] body) {
            log.info("===========================request begin================================================");
            log.debug("URI         : {}", request.getURI());
            log.debug("Method      : {}", request.getMethod());
            log.debug("Headers     : {}", request.getHeaders() );
            log.debug("Request body: {}", new String(body, StandardCharsets.UTF_8));
            log.info("==========================request end================================================");
        }
    }


}
