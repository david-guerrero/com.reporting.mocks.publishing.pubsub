package com.reporting.mocks.publishing.pubsub;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class PubsubConfig {
    @Autowired
    private Environment environment;

    public String getProjectId() {
        return environment.getProperty("com.google.projectId");
    }

}
