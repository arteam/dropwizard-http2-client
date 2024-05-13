package com.github.arteam.dropwizard.http2.client.transport;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.common.base.MoreObjects;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.HttpClientTransport;
import org.eclipse.jetty.http2.client.HTTP2Client;
import org.eclipse.jetty.http2.client.http.HttpClientTransportOverHTTP2;
import org.eclipse.jetty.util.ssl.SslContextFactory;

import javax.annotation.Nullable;

/**
 * Date: 11/26/15
 * Time: 10:10 AM
 * <p>
 * A {@link ClientTransportFactory} implementation that provides {@link HttpClientTransport}
 * as HTTP/2 and no SSL support.
 * <p>
 * Implements <b>h2c</b> transport for {@link HttpClient}.
 *
 * @author Artem Prigoda
 */
@JsonTypeName("h2c")
public class Http2ClearClientTransportFactory implements ClientTransportFactory {

    @Override
    @Nullable
    public SslContextFactory.Client sslContextFactory() {
        return null;
    }

    @Override
    public HttpClientTransport httpClientTransport() {
        // Explicitly set the HTTP/2 connection factory, because we don't need SSL and ALPN.
        final HTTP2Client client = new HTTP2Client();
        return new HttpClientTransportOverHTTP2(client);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper("h2c")
                .toString();
    }
}
