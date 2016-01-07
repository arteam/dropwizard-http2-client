package com.github.arteam.dropwizard.http2.client.transport;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.HttpDestination;
import org.eclipse.jetty.client.HttpExchange;
import org.eclipse.jetty.client.Origin;
import org.eclipse.jetty.http2.client.HTTP2Client;
import org.eclipse.jetty.http2.client.http.HttpClientTransportOverHTTP2;
import org.eclipse.jetty.http2.client.http.HttpConnectionOverHTTP2;
import org.eclipse.jetty.http2.client.http.HttpDestinationOverHTTP2;

import javax.annotation.Nullable;

/**
 * Date: 11/29/15
 * Time: 10:26 PM
 * <p>
 * An {@link HttpClientTransportOverHTTP2} implementation that tracks HTTP/2 calls
 * timings to the provided {@link MetricRegistry}.
 *
 * @author Artem Prigoda
 */
class InstrumentedHttpClientTransportOverHttp2 extends HttpClientTransportOverHTTP2 {

    private final MetricRegistry metricRegistry;

    @Nullable
    private final String name;
    private HttpClient client;

    public InstrumentedHttpClientTransportOverHttp2(HTTP2Client client, MetricRegistry metricRegistry,
                                                    @Nullable String name) {
        super(client);
        this.metricRegistry = metricRegistry;
        this.name = name;
    }

    @Override
    public void setHttpClient(HttpClient client) {
        super.setHttpClient(client);
        this.client = client;
    }

    @Override
    public HttpDestination newHttpDestination(Origin origin) {
        return new HttpDestinationOverHTTP2(client, origin) {
            @Override
            protected void send(HttpConnectionOverHTTP2 connection, HttpExchange exchange) {
                Timer timer = metricRegistry.timer(MetricRegistry.name(HTTP2Client.class, name,
                        origin.getAddress().getHost()));
                try (Timer.Context context = timer.time()) {
                    super.send(connection, exchange);
                }
            }
        };
    }
}
