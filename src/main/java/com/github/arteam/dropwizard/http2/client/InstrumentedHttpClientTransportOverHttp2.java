package com.github.arteam.dropwizard.http2.client;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.HttpDestination;
import org.eclipse.jetty.client.Origin;
import org.eclipse.jetty.http2.client.HTTP2Client;
import org.eclipse.jetty.http2.client.http.HttpClientTransportOverHTTP2;

/**
 * Date: 11/29/15
 * Time: 10:26 PM
 *
 * @author Artem Prigoda
 */
public class InstrumentedHttpClientTransportOverHttp2 extends HttpClientTransportOverHTTP2 {

    private final MetricRegistry metricRegistry;
    private final String name;
    private HttpClient client;

    public InstrumentedHttpClientTransportOverHttp2(HTTP2Client client, MetricRegistry metricRegistry, String name) {
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
        Timer timer = metricRegistry.timer(MetricRegistry.name(HTTP2Client.class, name, origin.getAddress().getHost()));
        return new InstrumentedHttpDestinationOverHTTP2(client, origin, timer);
    }
}
