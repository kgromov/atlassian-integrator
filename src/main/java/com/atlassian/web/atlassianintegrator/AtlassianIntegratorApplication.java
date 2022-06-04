package com.atlassian.web.atlassianintegrator;

import com.atlassian.web.atlassianintegrator.jira.service.Authentication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class AtlassianIntegratorApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(AtlassianIntegratorApplication.class, args);
        Authentication authentication = context.getBean(Authentication.class);
        authentication.login();
    }

}
