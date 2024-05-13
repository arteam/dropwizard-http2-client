package com.github.arteam.dropwizard.http2.client.transport;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.dropwizard.jackson.Discoverable;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.HttpClientTransport;
import org.eclipse.jetty.util.ssl.SslContextFactory;

import javax.annotation.Nullable;

/**
 * Date: 11/26/15
 * Time: 10:11 AM
 * <p>
 * A service interface for discovering of factories for {@link HttpClientTransport}.
 * It allows dynamically plug-in different transports to {@link HttpClient}.
 *
 * @author Artem Prigoda
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type",
        defaultImpl = Http2ClientTransportFactory.class)
public interface ClientTransportFactory extends Discoverable {

    /**
     * Configures the SSL context for an HTTP/2 client.
     *
     * @return a configured {@link SslContextFactory}
     */
    @Nullable
    SslContextFactory.Client sslContextFactory();

    /**
     * Configures the transport implementation for an HTTP/2 client
     *
     * @return a configured {@link HttpClientTransport}
     */
    HttpClientTransport httpClientTransport();
}
