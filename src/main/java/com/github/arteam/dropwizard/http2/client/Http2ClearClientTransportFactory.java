package com.github.arteam.dropwizard.http2.client;

import com.codahale.metrics.MetricRegistry;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.common.base.MoreObjects;
import org.eclipse.jetty.client.HttpClientTransport;
import org.eclipse.jetty.http2.client.HTTP2Client;
import org.eclipse.jetty.http2.client.HTTP2ClientConnectionFactory;
import org.eclipse.jetty.http2.client.http.HttpClientTransportOverHTTP2;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.util.ssl.SslContextFactory;

import javax.annotation.Nullable;

/**
 * Date: 11/26/15
 * Time: 10:10 AM
 * <p>
 * A factory for {@link HttpClientTransportOverHTTP2}. Provides
 * <b>h2c</b> transport for {@link HttpClient}.
 *
 * @author Artem Prigoda
 */
@JsonTypeName("http2c")
public class Http2ClearClientTransportFactory implements ClientTransportFactory {

    @Override
    @Nullable
    public SslContextFactory sslContextFactory() {
        return null;
    }

    @Override
    public HttpClientTransport httpClientTransport(MetricRegistry metricRegistry, String name) {
        // Explicitly set the HTTP/2 connection factory, because we don't need SSL and ALPN.
        final HTTP2Client client = new HTTP2Client();
        client.setClientConnectionFactory(new HTTP2ClientConnectionFactory());
        return new InstrumentedHttpClientTransportOverHttp2(client, metricRegistry, name);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper("http2c")
                .toString();
    }
}
