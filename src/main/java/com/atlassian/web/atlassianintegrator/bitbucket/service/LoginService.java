package com.atlassian.web.atlassianintegrator.bitbucket.service;

import com.atlassian.web.atlassianintegrator.bitbucket.config.BitbucketCredentials;
import com.atlassian.web.atlassianintegrator.bitbucket.config.BitbucketJwtToken;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import static java.lang.String.format;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;


/*
Description	testin oauth
Key	JbhNemPVNX6MhdPPNS
Secret	KwFHUJNjZJUPLXf97kFyraQr6Bf35EEK
 */

@Service
public class LoginService {
    private final RestTemplate restTemplate;

    public LoginService( @Qualifier("bitbucketRestTemplate") RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /*
     * 1) get code on client callback url:
     * https://bitbucket.org/site/oauth2/authorize?client_id=JbhNemPVNX6MhdPPNS&response_type=code
     * 2) get code:
     * curl -X POST -u "client_id:secret"   https://bitbucket.org/site/oauth2/access_token  -d grant_type=authorization_code -d code={code}
     * 3) get token:  curl -X POST -u "JbhNemPVNX6MhdPPNS:KwFHUJNjZJUPLXf97kFyraQr6Bf35EEK"  https://bitbucket.org/site/oauth2/access_token -d grant_type=authorization_code -d code=yxxhtNALk6grfMHyhZ
     * 4) refresh token:
     * */

    public String getSecurityCode(BitbucketCredentials credentials) {
        Map<String, String> params = new HashMap<>();
        params.put("client_id", credentials.getClientId());
        params.put("response_type", "code");
        ResponseEntity<String> response = restTemplate.getForEntity(credentials.getAuthorizeUrl() + "authorize", String.class, params);
        return response.getBody();
    }

    public BitbucketJwtToken authorize(BitbucketCredentials credentials, String securityCode) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("grant_type", "authorization_code");
        requestBody.add("code", securityCode);

        ResponseEntity<BitbucketJwtToken> response = restTemplate.exchange(
            URI.create(credentials.getAuthorizeUrl() + "access_token"),
            POST,
            new HttpEntity<>(requestBody, headers),
            BitbucketJwtToken.class);
        return response.getBody();
    }

    public BitbucketJwtToken refreshToken(String refreshToken) {
        return null;
    }
}
