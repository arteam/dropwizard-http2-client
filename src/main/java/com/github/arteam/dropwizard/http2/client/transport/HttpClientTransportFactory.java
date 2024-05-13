package com.github.arteam.dropwizard.http2.client.transport;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.common.base.MoreObjects;
import org.eclipse.jetty.client.HttpClientTransport;
import org.eclipse.jetty.client.http.HttpClientTransportOverHTTP;

@JsonTypeName("http")
public class HttpClientTransportFactory implements ClientTransportFactory {

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
