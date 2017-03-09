package com.github.arteam.dropwizard.http2.client;

import com.codahale.metrics.Counter;
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.github.arteam.dropwizard.http2.client.names.NameStrategy;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.client.api.Response;

import static com.codahale.metrics.MetricRegistry.name;

public class InstrumentedListener extends Request.Listener.Adapter {

    private final MetricRegistry metricRegistry;
    private final NameStrategy name;

    /**
     * The time taken between when request data starts being sent and response
     * data is first received
     */
    private Timer timeToFirstByte;

    /**
     * Time taken to send all request data and receive all response data
     */
    private Timer total;

    private Timer.Context totalContext;
    private Timer.Context ttfbContext;
    private Timer.Context queueContext;

    /**
     * Number of requests that result in a successful status code
     */
    private Meter request2xx;

    /**
     * Number of requests that result in a client error status code
     */
    private Meter request4xx;

    /**
     * Number of requests that result in a server error status code
     */
    private Meter request5xx;

    /**
     * Number of requests that result in a non 2xx, 4xx, or 5xx status code
     */
    private Meter requestOtherStatus;

    /**
     * Number of requests that failed and did not receive a status code
     */
    private Meter requestException;

    /**
     * The number of requests in transit to the server
     */
    private Counter inflight;

    /**
     * The number of requests currently queued and ready to be sent
     */
    private Counter onQueue;

    public InstrumentedListener(MetricRegistry metricRegistry, NameStrategy name) {
        this.metricRegistry = metricRegistry;
        this.name = name;
    }

    @Override
    public void onQueued(Request request) {
        final String metricName = name.nameFor(request);
        final Timer queue = metricRegistry.timer(name(metricName, "queue-wait"));
        total = metricRegistry.timer(name(metricName, "total"));
        timeToFirstByte = metricRegistry.timer(name(metricName, "time-to-first-byte"));
        request2xx = metricRegistry.meter(name(metricName, "2xx"));
        request4xx = metricRegistry.meter(name(metricName, "4xx"));
        request5xx = metricRegistry.meter(name(metricName, "5xx"));
        requestOtherStatus = metricRegistry.meter(name(metricName, "other-status"));
        requestException = metricRegistry.meter(name(metricName, "exception"));
        inflight = metricRegistry.counter(name(metricName, "inflight"));
        onQueue = metricRegistry.counter(name(metricName, "on-queue"));
        onQueue.inc();
        queueContext = queue.time();
    }

    @Override
    public void onBegin(Request request) {
        onQueue.dec();
        queueContext.stop();
        totalContext = total.time();
    }

    @Override
    public void onCommit(Request request) {
        ttfbContext = timeToFirstByte.time();
        inflight.inc();
    }

    public void onResponseBegin() {
        ttfbContext.stop();
    }

    public void onResponseComplete(Throwable exn, Response response) {
        totalContext.stop();
        inflight.dec();

        if (exn != null) {
            requestException.mark();
        } else {
            switch (javax.ws.rs.core.Response.Status.Family.familyOf(response.getStatus())) {
                case SUCCESSFUL:
                    request2xx.mark();
                    break;
                case SERVER_ERROR:
                    request5xx.mark();
                    break;
                case CLIENT_ERROR:
                    request4xx.mark();
                    break;
                default:
                    requestOtherStatus.mark();
            }
        }
    }
}
