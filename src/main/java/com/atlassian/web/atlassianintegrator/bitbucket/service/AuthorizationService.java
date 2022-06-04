package com.atlassian.web.atlassianintegrator.bitbucket.service;

import com.atlassian.web.atlassianintegrator.bitbucket.config.BitbucketCredentials;
import com.atlassian.web.atlassianintegrator.bitbucket.config.BitbucketJwtToken;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

import static com.atlassian.web.atlassianintegrator.bitbucket.domain.GrantType.CLIENT_CREDENTIALS;
import static com.atlassian.web.atlassianintegrator.bitbucket.domain.GrantType.REFRESH_TOKEN;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;

@Service
public class AuthorizationService {
    private final RestTemplate restTemplate;

    public AuthorizationService( @Qualifier("notAuthorized") RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /*
     *  curl -X POST -u "client_id:secret" \
     * https://bitbucket.org/site/oauth2/access_token \
     * -d grant_type=client_credentials
     */

    public BitbucketJwtToken authorize(BitbucketCredentials credentials) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("grant_type", CLIENT_CREDENTIALS.getGrantType());

        ResponseEntity<BitbucketJwtToken> response = restTemplate.exchange(
            URI.create(credentials.getAuthorizeUrl() + "/access_token"),
            POST,
            new HttpEntity<>(requestBody, headers),
            BitbucketJwtToken.class);
        return response.getBody();
    }

    /*
    * curl -X POST -u "client_id:secret" \
    * https://bitbucket.org/site/oauth2/access_token \
    * -d grant_type=refresh_token -d refresh_token={refresh_token}
    */
    public BitbucketJwtToken refreshToken(BitbucketCredentials credentials, String refreshToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("grant_type", REFRESH_TOKEN.getGrantType());
        requestBody.add("refresh_token", refreshToken);

        ResponseEntity<BitbucketJwtToken> response = restTemplate.exchange(
            URI.create(credentials.getAuthorizeUrl() + "/access_token"),
            POST,
            new HttpEntity<>(requestBody, headers),
            BitbucketJwtToken.class);
        return response.getBody();
    }
}
