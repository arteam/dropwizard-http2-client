package com.github.arteam.dropwizard.http2.client.names;

import org.eclipse.jetty.client.api.Request;

@FunctionalInterface
public interface NameStrategy {
    /**
     * Determines the metric name to prefix request specific metrics (like
     * time to first byte) based on request properties.
     *
     * @param request The request that the name should be created for
     * @return The metric name
     */
    String nameFor(Request request);
}
