package com.github.arteam.dropwizard.http2.client;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.HttpClientTransport;
import org.eclipse.jetty.client.HttpConversation;
import org.eclipse.jetty.client.HttpRequest;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.api.Response;
import org.eclipse.jetty.http2.client.HTTP2Client;
import org.eclipse.jetty.util.ssl.SslContextFactory;

import java.net.URI;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

/**
 * An implementation of {@link HttpClient} which saves registers timing of calls
 * to external services
 *
 * @author Artem Prigoda (a.prigoda)
 * @since 26.03.16
 */
public class InstrumentedHttpClient extends HttpClient {

    private final MetricRegistry metricRegistry;
    private final String name;

    public InstrumentedHttpClient(HttpClientTransport transport, SslContextFactory sslContextFactory,
                                  MetricRegistry metricRegistry, String name) {
        super(transport, sslContextFactory);
        this.metricRegistry = metricRegistry;
        this.name = name;
    }

    @Override
    protected HttpRequest newHttpRequest(HttpConversation conversation, URI uri) {
        return new HttpRequest(this, conversation, uri) {
            @Override
            public ContentResponse send() throws InterruptedException, TimeoutException, ExecutionException {
                try (Timer.Context context = timer(uri).time()) {
                    return super.send();
                }
            }

            @Override
            public void send(Response.CompleteListener listener) {
                try (Timer.Context context = timer(uri).time()) {
                    super.send(listener);
                }
            }
        };
    }

    private Timer timer(URI uri) {
        return metricRegistry.timer(MetricRegistry.name(HTTP2Client.class, name, uri.getHost()));
    }
}
