package com.reporting.mocks.publishing.pubsub;

import org.springframework.stereotype.Component;

@Component
public class PubsubConfig {
    public String getProjectId() { return "<GOOGLE_PROJECT_ID>"; }
}
