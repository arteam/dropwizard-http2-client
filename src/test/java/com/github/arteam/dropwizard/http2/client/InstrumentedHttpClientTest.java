package com.github.arteam.dropwizard.http2.client;

import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.github.arteam.dropwizard.http2.client.names.NameStrategies;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit5.DropwizardAppExtension;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import org.assertj.core.api.SoftAssertions;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.http.HttpClientTransportOverHTTP;
import org.eclipse.jetty.http2.client.HTTP2Client;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(DropwizardExtensionsSupport.class)
public class InstrumentedHttpClientTest {
    public static final DropwizardAppExtension<TestConfiguration> rule = new DropwizardAppExtension<>(TestApplication.class,
            ResourceHelpers.resourceFilePath("server.yml"));

    private final MetricRegistry metrics = new MetricRegistry();
    private HttpClient client;

    @AfterEach
    public void after() throws Exception {
        if (client != null) {
            client.stop();
        }
    }

    @Test
    public void testInstrumentation() throws Exception {
        client = new InstrumentedHttpClient(new HttpClientTransportOverHTTP(), metrics, NameStrategies.prefixedStrategy(MetricRegistry.name(HTTP2Client.class), NameStrategies.HOST));
        client.start();
        client.GET(String.format("http://127.0.0.1:%d/application/greet-chunk", rule.getLocalPort())).getContentAsString();
        final Timer ttfb = metrics.getTimers().get("org.eclipse.jetty.http2.client.HTTP2Client.127.0.0.1.time-to-first-byte");
        final Timer queueWait = metrics.getTimers().get("org.eclipse.jetty.http2.client.HTTP2Client.127.0.0.1.queue-wait");
        final Timer total = metrics.getTimers().get("org.eclipse.jetty.http2.client.HTTP2Client.127.0.0.1.total");
        final Meter request2xx = metrics.getMeters().get("org.eclipse.jetty.http2.client.HTTP2Client.127.0.0.1.2xx");
        final Meter request5xx = metrics.getMeters().get("org.eclipse.jetty.http2.client.HTTP2Client.127.0.0.1.5xx");
        final Meter request4xx = metrics.getMeters().get("org.eclipse.jetty.http2.client.HTTP2Client.127.0.0.1.4xx");
        final Meter otherStatus = metrics.getMeters().get("org.eclipse.jetty.http2.client.HTTP2Client.127.0.0.1.other-status");
        final Meter exception = metrics.getMeters().get("org.eclipse.jetty.http2.client.HTTP2Client.127.0.0.1.exception");

        final SoftAssertions softly = new SoftAssertions();

        softly.assertThat(total.getSnapshot().getMedian())
                .isGreaterThanOrEqualTo(Duration.ofMillis(2500).toNanos())
                .isLessThan(Duration.ofMillis(3000).toNanos());

        softly.assertThat(ttfb.getSnapshot().getMedian())
                .isGreaterThanOrEqualTo(Duration.ofMillis(500).toNanos())
                .isLessThan(Duration.ofMillis(1000).toNanos());

        softly.assertThat(queueWait.getSnapshot().getMedian())
                .isGreaterThan(1)
                .isLessThan(Duration.ofMillis(500).toNanos());

        softly.assertThat(request2xx.getCount()).isEqualTo(1);
        softly.assertThat(request4xx.getCount()).isEqualTo(0);
        softly.assertThat(request5xx.getCount()).isEqualTo(0);
        softly.assertThat(otherStatus.getCount()).isEqualTo(0);
        softly.assertThat(exception.getCount()).isEqualTo(0);

        softly.assertAll();
    }

    @Test
    public void testMethodStrategy() throws Exception {
        final MetricRegistry metrics = new MetricRegistry();
        client = new InstrumentedHttpClient(new HttpClientTransportOverHTTP(), metrics, NameStrategies.METHOD);
        client.start();
        client.GET(String.format("http://127.0.0.1:%d/application/greet-chunk", rule.getLocalPort())).getContentAsString();
        assertThat(metrics.getTimers()).containsKey("GET.time-to-first-byte");
    }

    @Test
    public void testFullNameStrategy() throws Exception {
        final MetricRegistry metrics = new MetricRegistry();
        client = new InstrumentedHttpClient(new HttpClientTransportOverHTTP(), metrics, NameStrategies.FULL);
        client.start();
        client.GET(String.format("http://127.0.0.1:%d/application/greet-chunk", rule.getLocalPort())).getContentAsString();
        assertThat(metrics.getTimers()).containsKey("127.0.0.1.GET./application/greet-chunk.time-to-first-byte");
    }
}
