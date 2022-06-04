package com.atlassian.web.atlassianintegrator.bitbucket.config;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
//@JsonIgnoreProperties(ignoreUnknown = true)
public class BitbucketJwtToken {
    private String scopes;
    private String access_token;
    private long expires_in;
    private String token_type;
    private String state;
    private String refresh_token;
}
