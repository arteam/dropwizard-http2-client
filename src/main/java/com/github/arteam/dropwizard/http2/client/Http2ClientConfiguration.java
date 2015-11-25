package com.github.arteam.dropwizard.http2.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.util.Duration;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

/**
 * The external configuration class for {@link Http2ClientBuilder}.
 */
public class Http2ClientConfiguration {

    @NotNull
    private Duration connectionTimeout = Duration.milliseconds(500);

    @NotNull
    private Duration idleTimeout = Duration.minutes(1);

    @NotNull
    private List<String> protocols = Arrays.asList("h2", "h2-17", "h2-16", "h2-15", "h2-14");

    @Min(1)
    private int selectors = 1;

    @JsonProperty
    public Duration getConnectionTimeout() {
        return connectionTimeout;
    }

    @JsonProperty
    public void setConnectionTimeout(Duration connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    @JsonProperty
    public Duration getIdleTimeout() {
        return idleTimeout;
    }

    @JsonProperty
    public void setIdleTimeout(Duration idleTimeout) {
        this.idleTimeout = idleTimeout;
    }

    @JsonProperty
    public List<String> getProtocols() {
        return protocols;
    }

    @JsonProperty
    public void setProtocols(List<String> protocols) {
        this.protocols = protocols;
    }

    @JsonProperty
    public int getSelectors() {
        return selectors;
    }

    @JsonProperty
    public void setSelectors(int selectors) {
        this.selectors = selectors;
    }
}
