package com.github.arteam.dropwizard.http2.client;


import io.dropwizard.lifecycle.Managed;
import io.dropwizard.setup.Environment;
import org.eclipse.jetty.http2.client.HTTP2Client;

public class Http2ClientBuilder {

    private Environment environment;
    private Http2ClientConfiguration configuration;

    public Http2ClientBuilder(Environment environment) {
        this.environment = environment;
    }

    public Http2ClientBuilder using(Http2ClientConfiguration configuration) {
        this.configuration = configuration;
        return this;
    }

    public HTTP2Client build() {
        HTTP2Client http2Client = new HTTP2Client();
        http2Client.setClientConnectionFactory(configuration.getConnectionFactoryBuilder().build());
        http2Client.setConnectTimeout(configuration.getConnectionTimeout().toMilliseconds());
        http2Client.setIdleTimeout(configuration.getIdleTimeout().toMilliseconds());
        http2Client.setSelectors(configuration.getSelectors());

        environment.lifecycle().manage(new Managed() {
            @Override
            public void start() throws Exception {
                http2Client.start();
            }

            @Override
            public void stop() throws Exception {
                http2Client.stop();
            }
        });
        return http2Client;
    }
}
