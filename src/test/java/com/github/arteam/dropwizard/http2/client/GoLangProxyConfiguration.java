package com.github.arteam.dropwizard.http2.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;

/**
 * @author Artem Prigoda (a.prigoda)
 * @since 26.03.16
 */
public class GoLangProxyConfiguration extends Configuration {

    private JettyClientConfiguration h2Client;

    @JsonProperty
    public JettyClientConfiguration getH2Client() {
        return h2Client;
    }

    @JsonProperty
    public void setH2Client(JettyClientConfiguration h2Client) {
        this.h2Client = h2Client;
    }
}
