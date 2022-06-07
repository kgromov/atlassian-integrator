package com.atlassian.web.atlassianintegrator;

import com.atlassian.web.atlassianintegrator.bitbucket.config.BitbucketCredentials;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(BitbucketCredentials.class)
public class AtlassianIntegratorApplication {

    public static void main(String[] args) {
       SpringApplication.run(AtlassianIntegratorApplication.class, args);
    }
}
