package com.github.arteam.dropwizard.http2.client;


import io.dropwizard.setup.Environment;
import org.eclipse.jetty.http2.client.HTTP2Client;
import org.eclipse.jetty.http2.client.HTTP2ClientConnectionFactory;
import org.eclipse.jetty.io.ClientConnectionFactory;

public class Http2ClientBuilder {

    private Environment environment;
    private Http2ClientConfiguration configuration;
    private ClientConnectionFactory clientConnectionFactory;

    public Http2ClientBuilder(Environment environment) {
        this.environment = environment;
    }

    public Http2ClientBuilder using(Http2ClientConfiguration configuration) {
        this.configuration = configuration;
        return this;
    }

    public Http2ClientBuilder using(HTTP2ClientConnectionFactory clientConnectionFactory) {
        this.clientConnectionFactory = clientConnectionFactory;
        return this;
    }

    public HTTP2Client build() {
        HTTP2Client http2Client = new HTTP2Client();
        http2Client.setClientConnectionFactory(clientConnectionFactory);
        http2Client.setConnectTimeout(configuration.getConnectionTimeout().toMilliseconds());
        http2Client.setIdleTimeout(configuration.getIdleTimeout().toMilliseconds());
        http2Client.setProtocols(configuration.getProtocols());
        http2Client.setSelectors(configuration.getSelectors());
        try {
            http2Client.start();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        return http2Client;
    }
}
