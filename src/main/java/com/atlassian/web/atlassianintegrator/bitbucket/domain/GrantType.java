package com.atlassian.web.atlassianintegrator.bitbucket.domain;

public enum GrantType {
    AUTHORIZATION_CODE("authorization_code"),
    CLIENT_CREDENTIALS("client_credentials"),
    USER_PASSWORD("user_password"),
    REFRESH_TOKEN("refresh_token");

    private final String grantType;

    GrantType(String grantType) {
        this.grantType = grantType;
    }

    public String getGrantType() {
        return grantType;
    }
}
