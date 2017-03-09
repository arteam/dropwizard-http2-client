package com.github.arteam.dropwizard.http2.client.transport;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.common.base.MoreObjects;
import org.eclipse.jetty.client.HttpClientTransport;
import org.eclipse.jetty.client.http.HttpClientTransportOverHTTP;
import org.eclipse.jetty.util.ssl.SslContextFactory;

import javax.annotation.Nullable;

@JsonTypeName("http")
public class HttpClientTransportFactory implements ClientTransportFactory {
    @Nullable
    @Override
    public SslContextFactory sslContextFactory() {
        return null;
    }

    @Override
    public HttpClientTransport httpClientTransport() {
        return new HttpClientTransportOverHTTP();
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper("http")
                .toString();
    }
}
