package com.atlassian.web.atlassianintegrator.jira.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(value = { "self" })
public class Board {
    private Integer id;
    private String name;
    @JsonIgnore
    private String self;
    private String type;
}
