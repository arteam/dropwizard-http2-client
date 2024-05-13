package com.github.arteam.dropwizard.http2.client.transport;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.common.base.MoreObjects;
import org.eclipse.jetty.client.HttpClientTransport;
import org.eclipse.jetty.client.http.HttpClientTransportOverHTTP;
import org.eclipse.jetty.io.ClientConnector;

@JsonTypeName("https")
public class HttpsClientTransportFactory extends HttpsClientFactory implements ClientTransportFactory {
    @Override
    public HttpClientTransport httpClientTransport() {
        ClientConnector connector = new ClientConnector();
        connector.setSslContextFactory(sslContextFactory());
        return new HttpClientTransportOverHTTP(connector);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper("https")
                .add("tls", super.toString())
                .toString();
    }
}
