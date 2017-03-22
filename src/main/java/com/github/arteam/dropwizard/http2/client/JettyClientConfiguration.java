package com.github.arteam.dropwizard.http2.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.arteam.dropwizard.http2.client.transport.ClientTransportFactory;
import com.github.arteam.dropwizard.http2.client.transport.Http2ClientTransportFactory;
import com.google.common.base.MoreObjects;
import io.dropwizard.util.Duration;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * The external configuration class for {@link JettyClientBuilder}.
 */
public class JettyClientConfiguration {

    /**
     * The max waiting time to connect to a destination
     */
    @NotNull
    private Duration connectionTimeout = Duration.milliseconds(500);

    /**
     * The max time a connection can be idle. It's the request timeout as well.
     */
    @NotNull
    private Duration idleTimeout = Duration.minutes(1);

    /**
     * Should the client save cookies
     */
    private boolean storeCookies = false;

    /**
     * Should the client follow redirects
     */
    private boolean followRedirects = true;

    /**
     * A factory for providing the transport and SSL configuration for the client
     */
    @NotNull
    @Valid
    private ClientTransportFactory connectionFactoryBuilder = new Http2ClientTransportFactory();

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

    @JsonProperty("connectionFactory")
    public ClientTransportFactory getConnectionFactoryBuilder() {
        return connectionFactoryBuilder;
    }

    @JsonProperty("connectionFactory")
    public void setConnectionFactoryBuilder(ClientTransportFactory connectionFactoryBuilder) {
        this.connectionFactoryBuilder = connectionFactoryBuilder;
    }

    @JsonProperty
    public boolean isStoreCookies() {
        return storeCookies;
    }

    @JsonProperty
    public void setStoreCookies(boolean storeCookies) {
        this.storeCookies = storeCookies;
    }

    public boolean isFollowRedirects() {
        return followRedirects;
    }

    public void setFollowRedirects(boolean followRedirects) {
        this.followRedirects = followRedirects;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("connectionTimeout", connectionTimeout)
                .add("idleTimeout", idleTimeout)
                .add("storeCookies", storeCookies)
                .add("followRedirects", followRedirects)
                .add("connectionFactoryBuilder", connectionFactoryBuilder)
                .toString();
    }
}
