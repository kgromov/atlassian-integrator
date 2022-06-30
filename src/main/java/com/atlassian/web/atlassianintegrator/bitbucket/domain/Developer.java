package com.atlassian.web.atlassianintegrator.bitbucket.domain;

import lombok.Data;

@Data
public class Developer {
    private String nickname;
    private long merged;
}
