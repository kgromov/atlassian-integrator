package com.atlassian.web.atlassianintegrator.jira.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sprint {
//    private final Set<Ticket> tickets = new HashSet<>();
    private Integer id;
    private String name;
    private String self;
    private String goal;
}
