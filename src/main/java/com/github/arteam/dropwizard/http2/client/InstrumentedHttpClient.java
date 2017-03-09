package com.github.arteam.dropwizard.http2.client;

import com.codahale.metrics.MetricRegistry;
import com.github.arteam.dropwizard.http2.client.names.NameStrategy;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.HttpClientTransport;
import org.eclipse.jetty.client.HttpConversation;
import org.eclipse.jetty.client.HttpRequest;
import org.eclipse.jetty.util.ssl.SslContextFactory;

import java.net.URI;

/**
 * An implementation of {@link HttpClient} which saves registers timing of calls
 * to external services
 *
 * @author Artem Prigoda (a.prigoda)
 * @since 26.03.16
 */
public class InstrumentedHttpClient extends HttpClient {

    private final MetricRegistry metricRegistry;
    private final NameStrategy nameStrategy;

    public InstrumentedHttpClient(
            HttpClientTransport transport,
            SslContextFactory sslContextFactory,
            MetricRegistry metricRegistry,
            NameStrategy nameStrategy
    ) {
        super(transport, sslContextFactory);
        this.metricRegistry = metricRegistry;
        this.nameStrategy = nameStrategy;
    }

    @Override
    protected HttpRequest newHttpRequest(HttpConversation conversation, URI uri) {
        final HttpRequest req = super.newHttpRequest(conversation, uri);
        final InstrumentedListener listener = new InstrumentedListener(metricRegistry, nameStrategy);
        req.listener(listener);
        req.onResponseBegin((response) -> listener.onResponseBegin());
        req.onResponseSuccess((result) -> listener.onResponseComplete(null, result));
        req.onResponseFailure((x, exn) -> listener.onResponseComplete(exn, x));
        return req;
    }
}
