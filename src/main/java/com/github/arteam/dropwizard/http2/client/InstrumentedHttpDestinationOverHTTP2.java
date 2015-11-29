package com.github.arteam.dropwizard.http2.client;

import com.codahale.metrics.Timer;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.HttpExchange;
import org.eclipse.jetty.client.Origin;
import org.eclipse.jetty.http2.client.http.HttpConnectionOverHTTP2;
import org.eclipse.jetty.http2.client.http.HttpDestinationOverHTTP2;

/**
 * Date: 11/29/15
 * Time: 10:28 PM
 *
 * @author Artem Prigoda
 */
public class InstrumentedHttpDestinationOverHTTP2 extends HttpDestinationOverHTTP2 {

    private Timer timer;

    public InstrumentedHttpDestinationOverHTTP2(HttpClient client, Origin origin, Timer timer) {
        super(client, origin);
        this.timer = timer;
    }

    public InstrumentedHttpDestinationOverHTTP2(HttpClient client, Origin origin) {
        super(client, origin);
    }

    @Override
    protected void send(HttpConnectionOverHTTP2 connection, HttpExchange exchange) {
        try (Timer.Context time = timer.time()) {
            super.send(connection, exchange);
        }
    }
}
