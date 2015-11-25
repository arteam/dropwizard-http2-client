package com.github.arteam.dropwizard.http2.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import io.dropwizard.util.Duration;

import javax.validation.Valid;
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

    @Min(1)
    private int selectors = 1;

    @NotNull
    @Valid
    private ClientConnectionFactoryBuilder connectionFactoryBuilder = new Http2ClientConnectionFactoryBuilder();

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
    public int getSelectors() {
        return selectors;
    }

    @JsonProperty
    public void setSelectors(int selectors) {
        this.selectors = selectors;
    }

    @JsonProperty("connectionFactory")
    public ClientConnectionFactoryBuilder getConnectionFactoryBuilder() {
        return connectionFactoryBuilder;
    }

    @JsonProperty("connectionFactory")
    public void setConnectionFactoryBuilder(ClientConnectionFactoryBuilder connectionFactoryBuilder) {
        this.connectionFactoryBuilder = connectionFactoryBuilder;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("connectionTimeout", connectionTimeout)
                .add("idleTimeout", idleTimeout)
                .add("selectors", selectors)
                .add("connectionFactoryBuilder", connectionFactoryBuilder)
                .toString();
    }
}
